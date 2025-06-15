package com.seravian.feat_profile.presentation.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.components.CustomButton
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseScreen
import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_profile.domain.utils.Role
import com.seravian.feat_profile.R
import com.seravian.feat_profile.presentation.ProfileAction
import com.seravian.feat_profile.presentation.ProfileState
import com.seravian.feat_profile.presentation.components.InfoRow
import com.seravian.feat_profile.presentation.model.toProfileUI
import com.seravian.feat_profile.presentation.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    navigateToVerificationRequests: () -> Unit = {},
) {
    BaseScreen<ProfileViewModel>(
        enableLifecycleObservation = true,
        onCreateAction = { viewModel ->
            viewModel.profileAction(ProfileAction.LoadInfo)
        },
    ) { viewModel ->
        val profileState by viewModel.profileState.collectAsStateWithLifecycle()

        ProfileContent(
            profileState = profileState,
            profileAction = {
                when (it) {
                    ProfileAction.GoToVerificationRequests -> navigateToVerificationRequests()
                    else -> {}
                }
                viewModel.profileAction(it)
            },
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
    val languages = listOf("en" to "English", "ar" to "العربية")
    var isLanguageSelectorExpanded by remember { mutableStateOf(false) }
    var profileUI = profileState.profile?.toProfileUI()

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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(modifiedPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Avatar
            if (profileUI?.profileImageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profileUI.profileImageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_account_circle),
                    contentDescription = stringResource(R.string.avatar),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_account_circle),
                    contentDescription = stringResource(R.string.avatar),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(
                    R.string.welcome,
                    profileUI?.fullName ?: ""
                ),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = profileState.profile?.toProfileUI()?.email ?: "",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            profileUI?.gender?.let {
                InfoRow(
                    icon = if (it == Gender.MALE) painterResource(R.drawable.ic_male_face)
                    else painterResource(R.drawable.ic_female_face),
                    label = stringResource(R.string.gender),
                    value = it.value
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            profileUI?.dateOfBirth?.takeIf { it.isNotBlank() }?.let {
                InfoRow(
                    icon = painterResource(R.drawable.ic_bd_cake),
                    label = stringResource(R.string.date_of_birth),
                    value = it
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            InfoRow(
                icon = painterResource(R.drawable.ic_calendar_today),
                label = stringResource(R.string.member_since),
                value = profileUI?.accountSince ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Theme Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.dark_theme),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = profileState.isDarkTheme,
                    onCheckedChange = { profileAction(ProfileAction.UpdateTheme(it)) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Language Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.language),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Box {
                    OutlinedButton(
                        onClick = { isLanguageSelectorExpanded = true },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = languages.first { it.first == profileState.currentLanguage }.second,
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
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
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Role-based Content
            profileUI?.role?.let { role ->
                when (role) {
                    Role.DOCTOR -> {
                        Text(
                            text = stringResource(R.string.doctor_information),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                profileUI.doctorTitle?.let {
                                    InfoRow(
                                        icon = painterResource(R.drawable.ic_person),
                                        label = stringResource(R.string.title),
                                        value = it.title
                                    )
                                }

                                profileUI.doctorDescription?.takeIf { it.isNotBlank() }?.let {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    InfoRow(
                                        icon = painterResource(R.drawable.ic_description),
                                        label = stringResource(R.string.description),
                                        value = it
                                    )
                                }

                                profileUI.doctorSessionPrice?.let {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    InfoRow(
                                        icon = painterResource(R.drawable.ic_attach_money),
                                        label = stringResource(R.string.session_price),
                                        value = it
                                    )
                                }

                                profileUI.doctorVerifiedAt?.let {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    InfoRow(
                                        icon = painterResource(R.drawable.ic_verified_user),
                                        label = stringResource(R.string.verified_at),
                                        value = it
                                    )
                                }

                                OutlinedButton(
                                    onClick = { profileAction(ProfileAction.GoToVerificationRequests) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_verified),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.view_verification_requests))
                                }
                            }
                        }
                    }

                    Role.PATIENT -> {

                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    ProfileContent(
        profileState = ProfileState(),
        profileAction = {},
        baseAction = {}
    )
}