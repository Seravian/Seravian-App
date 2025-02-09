package com.greenvenom.auth.component

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenvenom.auth.R
import com.greenvenom.ui.theme.AppTheme

@Composable
fun AuthTextField(
    value: String,
    label: String,
    error: String,
    isPasswordField: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = value,
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
            } else null,
            isError = error.isNotEmpty(),
            shape = RoundedCornerShape(15.dp),
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth(),
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }
    }
}

@PreviewLightDark
@Composable
fun CustomTextFieldPreview() {
    AppTheme {
        AuthTextField(
            value = "",
            label = "Password",
            error = "",
            isPasswordField = true,
            onValueChange = {},
            keyboardOptions = KeyboardOptions()
        )
    }
}
