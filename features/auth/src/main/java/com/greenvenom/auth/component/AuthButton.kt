package com.greenvenom.auth.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenvenom.ui.theme.AppTheme

@Composable
fun AuthCustomButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    FilledTonalButton (
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),

        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 19.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(6.dp)
        )
    }
}

@PreviewLightDark
@Preview
@Composable
private fun ButtonPreview() {
    AppTheme {
        AuthCustomButton(
            text = "Login",
            onClick = {  }
        )
    }
}