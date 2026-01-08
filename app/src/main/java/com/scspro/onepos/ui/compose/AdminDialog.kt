package com.scspro.onepos.ui.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.scspro.onepos.model.Category
import com.scspro.onepos.model.Product
import com.scspro.onepos.ui.theme.SCSProBlack
import com.scspro.onepos.ui.theme.SCSProGold

////////////////////////////////////////////////////////////////////////////////////////////////////
// 관리자 비밀번호 입력 다이어로그 Composable Components
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun PwdInputDialog(onDismiss: () -> Unit, onSuccess: () -> Unit) {
    // 비밀번호 상태 - 사용자 비번 입력 값
    var pwdInput by remember { mutableStateOf("")}
    // 비밀번호 검증 상태 - 입력 오류 메시지 표시 용
    var isError by remember { mutableStateOf(false)}
    // 실제 관리자 비밀번호
    val ADMIN_PWD = "2318"

    //  ==== 비밀번호 확인 로직 ======================================================================
    val onConfirm = {
        if(pwdInput == ADMIN_PWD) {
            isError = false
            onSuccess()
        } else {
            isError = true
            pwdInput = ""   //실패 시 입력 초기화
        }
    }
    // ==== 비밀번호 Dialog Overlay ======================================================================
    Dialog(
        onDismiss,      //onDismissRequest -> Dialog 외부 클릭/뒤로가기 시 요청
        properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false)    // Dialog창 외부 클릭 시 취소 비활성화
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column (
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ==== 비밀번호 Dialog 제목 =========================================================
                SectionTitle("관리자 비밀번호 입력")

                Spacer(modifier = Modifier.height(10.dp))
                // ==== 비밀번호 입력 필드 ===========================================================
                OutlinedTextField(
                    value = pwdInput,
                    onValueChange = { //숫자만 입력받도록
                        if(it.all { char -> char.isDigit() }) pwdInput = it
                        isError = false //입력 시 오류 상태 해제
                    },
                    isError = isError,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),  //숫자만 입력
//                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = SCSProGold,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // ==== 비밀번호 불일치 시 ===========================================================
                if (isError) {
                    Text (
                        text = "비밀번호 불일치",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // ==== 취소 & 확인 버튼 =============================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp) //Arrangement.End(오른쪽 정렬) -> 가운데 정렬 변경(버튼 사이 간격 10.dp)
                ) {
                    // ==== 취소 버튼 ====
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f), //가로 꽉 채움
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SCSProGold)
                    ) {
                        Text("취소", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    // ==== 확인버튼 ====
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f), //가로 꽉 채움
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SCSProGold,
                            disabledContainerColor = SCSProGold.copy(alpha = 0.5f) //비활성화 시 연하게
                        ),
                        enabled = pwdInput.length >= 4  //비밀먼호 4자리 이상 입력시에만 확인버튼 활성화
                    ) {
                        Text("확인", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
// 키오스크 전환 다이어로그 Composable Components
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun ChangeKioskDialog(onDismiss: () -> Unit, onConfirm: () -> Unit, isCategoryEmpty: Boolean) {
    Dialog(
        onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false)    // Dialog창 외부 클릭 시 취소 비활성화
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ){
            Column(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ==== 키오스크 전환 Dialog 제목 =====================================================
                SectionTitle("키오스크로 전환하시겠습니까?")
                // ==== 카테고리 없을 시 ==============================================================
                if(isCategoryEmpty) {
                    Text (
                        text = "카테고리를 먼저 생성해주세요",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                // ==== 네 & 아니오 버튼 =============================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // ==== 아니오 버튼 ====
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),     //가로 꽉 채움
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SCSProGold)
                    ){
                        Text("아니오", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    // ==== 네 버튼 ====
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),     //가로 꽉 채움
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SCSProGold,
                            disabledContainerColor = SCSProGold.copy(alpha = 0.5f) //비활성화 시 연하게
                        ),
                        enabled = !isCategoryEmpty
                    ){
                        Text("네", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
// 카테고리 추가 Compasable Component
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun CategoryAddDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) { //onSave는 String 타입 인자 받음
    var categoryName by remember { mutableStateOf("") }
    Dialog(
        onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false)    // Dialog창 외부 클릭 시 취소 비활성화
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                // ==== 카테고리 추가 안내 문구 ========================================================
                SectionTitle("새 카테고리 이름 입력")
                Spacer(modifier = Modifier.height(10.dp))
                // ==== 새 카테고리 이름 입력 필드 =====================================================
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = SCSProGold,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                // ==== 취소 & 저장 버튼 =============================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // ==== 취소 버튼 =====
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),     //가로 꽉 채움
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SCSProGold)
                    ){
                        Text("취소", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    // ==== 저장 버튼 =====
                    Button(
                        onClick = { onSave(categoryName) }, //입력된 categoryName 부모 콜백함수 onSave에 전달
                        modifier = Modifier.weight(1f), //가로 꽉 채움
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SCSProGold,
                            disabledContainerColor = SCSProGold.copy(alpha = 0.5f) //비활성화 시 연하게
                        ),
                        enabled = categoryName.isNotEmpty() //한글자라도 입력해야 저장 버튼 활성화
                    ){
                        Text("저장", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

            }
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
// 상품 추가 Compasable Component
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun ProductAddDialog(currentPageCategoryId: Int, categories: List<Category>, onDismiss: () -> Unit, onSave: (Product) -> Unit) {
    var defaultImage = "default_product_image"
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableLongStateOf(0L) }
    var selectedCategoryId by remember { mutableIntStateOf(currentPageCategoryId) }
    //var selectedOptions by remember { }


    Dialog(
        onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false)
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // ==== 상품 사진 ====================================================================
                SectionTitle("상품 사진")
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color.Gray, RoundedCornerShape(5.dp))
                        .clickable { /* 이미지 선택 로직 */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text("클릭", color = Color.White, fontWeight = FontWeight.Bold)
                }

                // ==== 상품 이름 ====================================================================
                SectionTitle("상품 이름")
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = SCSProGold,
                        unfocusedBorderColor = Color.Gray
                    ),
                    placeholder = {
                        Text("상품 이름을 입력해주세요", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                // ==== 상품 가격 ====================================================================
                SectionTitle("상품 가격")
                OutlinedTextField(
                    value = if (productPrice == 0L) "" else String.format("%,d", productPrice),
                    onValueChange = { input ->
                        // 숫자 외, 모든 문자 제거
                        val numberOnly = input.replace(Regex("[^0-9]"), "")
                        
                        // 입력 제한 및 Long 변환
                        if (numberOnly.length <= 12) { //Long 범위 고려
                            productPrice = numberOnly.toLongOrNull() ?: 0L
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = SCSProGold,
                        unfocusedBorderColor = Color.Gray
                    ),
                    suffix = {
                        Text("원", fontWeight = FontWeight.Bold, color = SCSProBlack, textAlign = TextAlign.Start)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text("0", Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                // ==== 상품 카테고리 선택 ============================================================
                SectionTitle("상품 카테고리")
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(categories) {category ->
                        val isSelected = selectedCategoryId == category.id
                        SelectableChip(
                            label = category.name,
                            isSelected = isSelected,
                            onClick = {
                                selectedCategoryId = category.id
                            }
                        )
                    }
                }
                // ==== 상품 옵션 ====================================================================
                SectionTitle("상품 옵션")
//                LazyRow(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(5.dp)
//                ) {
//                    items(options) { option ->
//
//
//                    }
//                }
                // ==== 취소 & 저장 버튼 =============================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // ==== 취소 버튼
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SCSProGold)
                    ) {
                        Text("취소", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = {
                            // 입력된 상태값들을 Product 모델로 변환
                            val newProduct = Product(
                                id = 0,
                                name = productName,
                                price = productPrice,
                                imageRes = null,        //추후 이미지 구현
                                categoryId = selectedCategoryId,
                                optionId = emptyList()          // 추후 옵션 설정 구현
                            )
                            Log.d("newProduct", newProduct.toString())
                            onSave(newProduct)
                        },
                        enabled = productName.isNotEmpty() && productPrice >= 0 && categories.any { it.id == selectedCategoryId },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SCSProGold, disabledContainerColor = SCSProGold.copy(alpha = 0.5f)
                        )
                    ){
                        Text( "저장", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

            }
        }
    }
}