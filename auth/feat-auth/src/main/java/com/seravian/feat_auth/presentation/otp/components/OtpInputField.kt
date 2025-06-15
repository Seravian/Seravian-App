package com.seravian.feat_auth.presentation.otp.components

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seravian.core_ui.theme.AppTheme
import com.seravian.core_ui.theme.onPrimaryContainerLight
import com.seravian.core_ui.theme.outlineLight
import com.seravian.core_ui.theme.primaryContainerLight

@Composable
fun OtpInputField(
    number: String?,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onNumberChanged: (String?) -> Unit,
    onKeyboardBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text by remember(number) {
        mutableStateOf(
            TextFieldValue(
                text = number?.toString().orEmpty(),
                selection = TextRange(
                    index = if(number != null) 1 else 0
                )
            )
        )
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = outlineLight
            )
            .background(primaryContainerLight),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newText ->
                val newCharacter = newText.text
                if(newCharacter.length <= 1) {
                    onNumberChanged(newCharacter)
                }
            },
            cursorBrush = SolidColor(onPrimaryContainerLight),
            singleLine = true,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = onPrimaryContainerLight
            ),
            modifier = Modifier
                .padding(10.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                    onFocusChanged(it.isFocused)
                }
                .onKeyEvent { event ->
                    val didPressDelete = event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL
                    if(didPressDelete && number == null) {
                        onKeyboardBack()
                    }
                    false
                },
            decorationBox = { innerBox ->
                innerBox()
                if(!isFocused && number == null) {
                    Text(
                        text = "-",
                        textAlign = TextAlign.Center,
                        color = onPrimaryContainerLight,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                    )
                }
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun OtpInputFieldPreview() {
    AppTheme {
        OtpInputField(
            number = "h",
            focusRequester = remember { FocusRequester() },
            onFocusChanged = {},
            onKeyboardBack = {},
            onNumberChanged = {},
            modifier = Modifier
                .size(100.dp)
        )
    }
}