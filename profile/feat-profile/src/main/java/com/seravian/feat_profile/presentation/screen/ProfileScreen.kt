package com.seravian.feat_profile.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seravian.feat_profile.presentation.model.ProfileUI
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.components.CustomButton
import com.greenvenom.core_ui.presentation.BaseScreen
import com.seravian.feat_profile.R
import com.seravian.feat_profile.presentation.viewModel.ProfileViewModel
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseAction
import com.seravian.feat_profile.presentation.ProfileAction
import com.seravian.feat_profile.presentation.ProfileState
import com.seravian.feat_profile.presentation.model.toProfileUI

@Composable
fun ProfileScreen() {
    BaseScreen<ProfileViewModel> { viewModel ->
        val profileState by viewModel.profileState.collectAsState()

        ProfileContent(
            profileState = profileState,
            profileAction = viewModel::profileAction,
            baseAction = viewModel::baseAction
        )
    }
}

@Composable
fun ProfileContent(
    profileState: ProfileState,
    profileAction: (ProfileAction) -> Unit,
    baseAction: (BaseAction) -> Unit
) {
    val profileUI = profileState.profile?.toProfileUI() ?: ProfileUI()
    val languages = listOf("en" to "English", "ar" to "العربية")
    var isLanguageSelectorExpanded by remember { mutableStateOf(false) }

    profileState.logoutResult
        ?.onSuccess {
            baseAction(BaseAction.HideLoading)
        }
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(it.errorType?.toString() ?: ""))
        }

    Scaffold(
        topBar = {
            TopAppBar(
                isVisible = true,
                isSideDestination = false,
                isActionEnabled = false
            )
        }
    ) { innerPadding ->
        val modifiedPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = 0.dp,
            start = 24.dp,
            end = 24.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(modifiedPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = stringResource(R.string.avatar),
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(
                    R.string.welcome,
                    profileUI.fullName
                ),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = profileUI.email,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Theme Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.dark_theme), fontSize = 16.sp)
                Switch(
                    checked = profileState.isDarkTheme,
                    onCheckedChange = { profileAction(ProfileAction.UpdateTheme(it)) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Language Selector
            Text(stringResource(R.string.language), fontSize = 16.sp)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { isLanguageSelectorExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(languages.first { it.first == profileState.currentLanguage }.second)
                }
                DropdownMenu(
                    expanded = isLanguageSelectorExpanded,
                    onDismissRequest = { isLanguageSelectorExpanded = false }
                ) {
                    languages.forEach { (code, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                profileAction(ProfileAction.UpdateLanguage(code))
                                isLanguageSelectorExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Logout Button
            CustomButton(
                text = stringResource(R.string.logout),
                onClick = {
                    baseAction(BaseAction.ShowLoading)
                    profileAction(ProfileAction.Logout)
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview(){
    ProfileContent(
        profileState = ProfileState(),
        profileAction = {},
        baseAction = {}
    )
}