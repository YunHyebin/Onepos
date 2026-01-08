package com.scspro.onepos.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.scspro.onepos.ui.theme.*
import java.time.format.TextStyle

/**
 * 관리자 화면에서 공통으로 사용되는 Component 모음
* */
////////////////////////////////////////////////////////////////////////////////////////////////////
// Dialog 창 안내 문구
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = SCSProBlack,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}
////////////////////////////////////////////////////////////////////////////////////////////////////
// 입력 필드
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AdminOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    textAlign: TextAlign = TextAlign.Center,
    keyboardOption: KeyboardOptions? = null,
    visualTransformation: VisualTransformation? = null,
    isError: Boolean = false,
    suffix: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(placeholder, modifier = Modifier.fillMaxWidth(), textAlign = textAlign, color = Color.LightGray)
        },
        suffix = suffix,
        singleLine = true,
        shape = RoundedCornerShape(5.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SCSProGold,     // 포커스 시 갈색
            unfocusedBorderColor = Color.Gray,   // 평상시 회색
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}
////////////////////////////////////////////////////////////////////////////////////////////////////
// 선택
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun SelectableChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(45.dp).clickable{ onClick() },
        shape = RoundedCornerShape(5.dp),
        color = if (isSelected) SCSProGold else SCSProGold.copy(alpha = 0.5f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = if(isSelected) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }
    }
}