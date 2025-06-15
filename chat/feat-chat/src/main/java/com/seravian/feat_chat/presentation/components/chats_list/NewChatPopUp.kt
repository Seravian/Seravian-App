package com.seravian.feat_chat.presentation.components.chats_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.seravian.core_ui.components.CustomButton
import com.seravian.core_ui.components.CustomTextField
import com.seravian.core_ui.theme.AppTheme
import com.seravian.feat_chat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatPopUp(
    value: String,
    onValueChange: (String) -> Unit,
    onCreateChat: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    isEdit: Boolean = false ,
    onEditChat: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        BasicAlertDialog (
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .padding(24.dp)
                    .width(350.dp)
                    .height(200.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    CustomTextField(
                        label = stringResource(R.string.chat_title_label),
                        value = value,
                        error = "",
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CustomButton(
                            text = if (isEdit)stringResource(R.string.edit_chat) else stringResource(R.string.create_chat),
                            onClick = if (isEdit) onEditChat else onCreateChat,
                            enabled = value.isNotEmpty(),
                            modifier = Modifier.weight(1f)
                        )
                        if (isEdit) {
                            Spacer(modifier = Modifier.width(8.dp))
                            CustomButton(
                                text = stringResource(R.string.delete),
                                onClick = onDelete,
                                enabled = value.isNotEmpty(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PopUpPreview() {
    AppTheme {
        NewChatPopUp(
            value = "fdfh",
            onValueChange = {  },
            onCreateChat = {  },
            onEditChat = {  },
            onDismiss = {  },
            onDelete = {  },
            isEdit = true
        )
    }
}