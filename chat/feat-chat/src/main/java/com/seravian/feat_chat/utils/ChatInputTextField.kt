package com.seravian.feat_chat.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seravian.feat_chat.R
import com.seravian.feat_chat.ui.theme.gray


@Composable
fun ChatInputTextField(state: MutableState<String>,modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = gray,
            focusedIndicatorColor = gray
        ),
        shape = RoundedCornerShape(topEnd = 20.dp),
        placeholder = {
            Text(text = stringResource(R.string.type_a_message))
        }, modifier = Modifier.padding(10.dp).fillMaxWidth(.65f)
    )
}

@Preview
@Composable
private fun ChatInputTextFieldPreview() {
    val state = remember {
        mutableStateOf ("")
    }
    ChatInputTextField(state)
}