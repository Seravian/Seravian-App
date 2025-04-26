package com.greenvenom.core_ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenvenom.core_ui.R
import com.greenvenom.core_ui.theme.AppTheme

@Composable
fun CustomTextField(
    value: String,
    label: String,
    error: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    isPasswordField: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column() {
        OutlinedTextField(
            value = value,
            readOnly = readOnly,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = true,
            visualTransformation = if (isPasswordField && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = if (isPasswordField) {
                {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            if (passwordVisible) R.drawable.visibility_off_ic
                            else R.drawable.visibility_ic
                        ),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                }
            } else trailingIcon,
            isError = error.isNotEmpty(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = keyboardOptions,
            modifier = modifier.fillMaxWidth()
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@PreviewLightDark
@Composable
fun CustomTextFieldPreview() {
    AppTheme {
        CustomTextField(
            value = "",
            label = "Password",
            error = "Something Went Wrong",
            isPasswordField = true,
            onValueChange = {},
            keyboardOptions = KeyboardOptions()
        )
    }
}
