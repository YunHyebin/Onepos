package com.scspro.onepos.ui.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.scspro.onepos.ui.theme.*
import java.time.format.TextStyle

/**
 * 관리자 화면에서 공통으로 사용되는 Component 모음
* */
////////////////////////////////////////////////////////////////////////////////////////////////////
// 공통 Dialog 베이스
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AdminBaseDialog(
    onDismiss: () -> Unit,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 20.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SectionTitle(title)
                Spacer(modifier = Modifier.height(10.dp))
                content()
            }
        }
    }
}
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
// 공통 입력 필드
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AdminCommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textAlign: TextAlign = TextAlign.Center, // 기본 정렬: 가운데
    suffix: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                modifier = Modifier.fillMaxWidth(),
                textAlign = textAlign,
                color = Color.LightGray
            )
        },
        suffix = suffix,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SCSProGold,
            unfocusedBorderColor = Color.Gray,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            errorBorderColor = Color.Red
        ),
        shape = RoundedCornerShape(5.dp)
    )
}
////////////////////////////////////////////////////////////////////////////////////////////////////
// 공통 선택
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun SelectableChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width((LocalConfiguration.current.screenWidthDp.dp / 4))
            .height(48.dp)
            .clickable{ onClick() },
        shape = RoundedCornerShape(5.dp),
        color = if (isSelected) SCSProGold else SCSProGold.copy(alpha = 0.5f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////
// 공통 버튼
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AdminCommonButton(
    text: String,
    onClick: () -> Unit,
    modifier : Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = SCSProGold
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f)
        )
    ) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
    }
}