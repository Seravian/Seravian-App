package com.seravian.feat_chat.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seravian.feat_chat.R

@Composable
fun ChatInputTextField(
    input: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = input,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(25.dp),
        placeholder = {
            Text(text = stringResource(R.string.type_a_message))
        },
        trailingIcon = trailingIcon,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatInputTextFieldPreview() {
    ChatInputTextField(
        input = "",
        onValueChange = {},
        trailingIcon = {
            IconButton(onClick = {}, enabled = true) {
                Icon(
                    painter = painterResource(R.drawable.ic_analyze_symptoms),
                    contentDescription = stringResource(R.string.analyze_symptoms)
                )
            }
        }
    )
}