package com.seravian.seravianapp.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seravian.seravianapp.ui.theme.SeravianTheme
import com.seravian.seravianapp.ui.theme.onPrimaryContainerLight
import com.seravian.seravianapp.ui.theme.primaryLight

@Composable
fun AuthCustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
    ) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryLight
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .padding(8.dp),
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = onPrimaryContainerLight,
            modifier = Modifier.padding(6.dp)
        )
    }
}

@PreviewLightDark
@Preview
@Composable
private fun ButtonPreview() {
    SeravianTheme {
        AuthCustomButton(
            text = "Login",
            onClick = {  }
        )
    }
}