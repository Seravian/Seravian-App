package com.seravian.seravianapp.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seravian.seravianapp.ui.theme.AuthBlueButtonColor
import com.seravian.seravianapp.ui.theme.bluePrimary

@Composable
fun AuthCustomButton(
    modifier: Modifier= Modifier,
    text:String,
    onClick:()->Unit,
    isLoading:Boolean
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AuthBlueButtonColor),
        shape = RoundedCornerShape(10.dp)
    ) {
        if (isLoading)
            CircularProgressIndicator(color = AuthBlueButtonColor)
        else
            Text(
                text = text,
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
    }
}