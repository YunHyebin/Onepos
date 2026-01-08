package com.scspro.onepos.ui.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
    var pwdInput by remember { mutableStateOf("") }     //비밀번호 입력 값 상태
    var isError by remember { mutableStateOf(false) }   //비밀번호 입력 오류 메시지 표시 상태
    val ADMIN_PWD = "2318"                                     // 실제 관리자 비밀번호

    //  ==== 비밀번호 확인 로직 ======================================================================
    val onConfirm = {
        if (pwdInput == ADMIN_PWD) {
            isError = false
            onSuccess()
        } else {
            isError = true
            pwdInput = ""
        }
    }

    // ==== 비밀번호 Dialog== ========================================================================
    AdminBaseDialog(
        onDismiss = onDismiss,  //취소 버튼 클릭 시
        title = "관리자 비밀번호 입력"
    ) {
        // ==== 비밀번호 입력 필드 ====================================================================
        AdminCommonTextField(
            value = pwdInput,
            onValueChange = {   //숫자만 입력 받도록
                if (it.all { char -> char.isDigit() }) pwdInput = it
                isError = false //입력 시 오류 상태 해제
            },
            placeholder = "비밀번호를 입력하세요",
            isError = isError,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),  //숫자만 입력
            textAlign = TextAlign.Center
        )

        // ==== 비밀번호 불일치 시========= ===========================================================
        if (isError) {
            Text(
                text = "비밀번호 불일치",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ==== 취소 & 확인 버튼 =====================================================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            AdminCommonButton(
                text = "취소",
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            )
            AdminCommonButton(
                text = "확인",
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                enabled = pwdInput.length >= 4
            )
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 키오스크 전환 다이어로그 Composable Components
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun ChangeKioskDialog(onDismiss: () -> Unit, onConfirm: () -> Unit, isCategoryEmpty: Boolean) {
    AdminBaseDialog(
        onDismiss = onDismiss,
        title = "키오스크로 전환하시겠습니까?"
    ) {
        if (isCategoryEmpty) {
            Text(
                text = "카테고리를 먼저 생성해주세요",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            AdminCommonButton(text = "아니오", onClick = onDismiss, modifier = Modifier.weight(1f))
            AdminCommonButton(text = "네", onClick = onConfirm, modifier = Modifier.weight(1f), enabled = !isCategoryEmpty)
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 카테고리 추가 Compasable Component
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun CategoryAddDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var categoryName by remember { mutableStateOf("") }

    AdminBaseDialog(
        onDismiss = onDismiss,
        title = "새 카테고리 이름 입력"
    ) {
        AdminCommonTextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            placeholder = "카테고리 이름을 입력하세요"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            AdminCommonButton(text = "취소", onClick = onDismiss, modifier = Modifier.weight(1f))
            AdminCommonButton(text = "저장", onClick = { onSave(categoryName) }, modifier = Modifier.weight(1f), enabled = categoryName.isNotEmpty())
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 상품 추가 Compasable Component
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun ProductAddDialog(currentPageCategoryId: Int, categories: List<Category>, onDismiss: () -> Unit, onSave: (Product) -> Unit) {
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableLongStateOf(0L) }
    var selectedCategoryId by remember { mutableIntStateOf(currentPageCategoryId) }

    AdminBaseDialog(
        onDismiss = onDismiss,
        title = "상품 정보 입력" // 공통 타이틀 적용
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ==== 상품 사진 ====
            SectionTitle("상품 사진")
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.Gray, androidx.compose.foundation.shape.RoundedCornerShape(5.dp))
                    .clickable { /* 이미지 선택 로직 */ },
                contentAlignment = Alignment.Center
            ) {
                Text("클릭", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // ==== 상품 이름 ====
            SectionTitle("상품 이름")
            AdminCommonTextField(
                value = productName,
                onValueChange = { productName = it },
                placeholder = "상품 이름을 입력해주세요"
            )

            // ==== 상품 가격 ====
            SectionTitle("상품 가격")
            AdminCommonTextField(
                value = if (productPrice == 0L) "" else String.format("%,d", productPrice),
                onValueChange = { input ->
                    val numberOnly = input.replace(Regex("[^0-9]"), "")
                    if (numberOnly.length <= 12) {
                        productPrice = numberOnly.toLongOrNull() ?: 0L
                    }
                },
                placeholder = "0",
                textAlign = TextAlign.Start,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text("원", fontWeight = FontWeight.Bold, color = SCSProBlack) }
            )

            // ==== 상품 카테고리 선택 ====
            SectionTitle("상품 카테고리")
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly
            ) {
                items(categories) { category ->
                    SelectableChip(
                        label = category.name,
                        isSelected = selectedCategoryId == category.id,
                        onClick = { selectedCategoryId = category.id }
                    )
                }
            }

            // ==== 취소 & 저장 버튼 ====
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                AdminCommonButton(text = "취소", onClick = onDismiss, modifier = Modifier.weight(1f))
                AdminCommonButton(
                    text = "저장",
                    onClick = {
                        val newProduct = Product(
                            id = 0, name = productName, price = productPrice,
                            imageRes = null, categoryId = selectedCategoryId, optionId = emptyList()
                        )
                        onSave(newProduct)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = productName.isNotEmpty() && productPrice >= 0 && categories.any { it.id == selectedCategoryId }
                )
            }
        }
    }
}