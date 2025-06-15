package com.seravian.seravianapp.navigation.graphs

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.greenvenom.core_navigation.data.NavigationType
import com.seravian.feat_chat.presentation.screen.ChatListScreen
import com.seravian.feat_chat.presentation.screen.ChatScreen
import com.seravian.feat_chat.presentation.screen.DiagnosesListScreen
import com.seravian.feat_chat.presentation.screen.DiagnosisDetailsScreen
import com.seravian.feat_chat.presentation.screen.VoiceModeScreen
import com.seravian.feat_doctors.presentation.screen.DoctorDetailsScreen
import com.seravian.feat_doctors.presentation.screen.DoctorsScreen
import com.seravian.feat_home.presentation.screens.HomeScreen
import com.seravian.feat_navigation.routes.Screen
import com.seravian.feat_navigation.routes.SubGraph
import com.seravian.feat_profile.presentation.screen.ProfileScreen

fun NavGraphBuilder.patientGraph(navigate: (NavigationType) -> Unit) {
    navigation<SubGraph.Patient>(startDestination = SubGraph.AIChat) {
        composable<Screen.Home> {
            HomeScreen(
                navigateBack = { navigate(NavigationType.Back) }
            )
        }

        navigation<SubGraph.AIChat>(startDestination = Screen.ChatsList) {
            composable<Screen.ChatsList> {
                ChatListScreen(
                    navigateToChat = { chatId ->
                        navigate(
                            NavigationType.Standard(Screen.Chat)
                        )
                    },
                    navigateBack = { navigate(NavigationType.Back) }
                )
            }

            composable<Screen.Chat> {
                ChatScreen(
                    navigateToVoiceMode = {
                        navigate(
                            NavigationType.Standard(Screen.VoiceMode)
                        )
                    },
                    navigateToDiagnosesList = {
                        navigate(
                            NavigationType.Standard(Screen.DiagnosesList)
                        )
                    },
                    navigateBack = { navigate(NavigationType.Back) }
                )
            }

            composable<Screen.VoiceMode> {
                VoiceModeScreen(
                    navigateBack = { navigate(NavigationType.Back) }
                )
            }

            composable<Screen.DiagnosesList> {
                DiagnosesListScreen(
                    navigateToDiagnosisDetails = {
                        navigate(
                            NavigationType.Standard(Screen.DiagnosisDetails)
                        )
                    },
                    navigateBack = { navigate(NavigationType.Back) }
                )
            }

            composable<Screen.DiagnosisDetails> {
                DiagnosisDetailsScreen(
                    navigateBack = { navigate(NavigationType.Back) }
                )
            }
        }

        composable<Screen.Sessions> {
            Text(text = "Sessions")
        }

        composable<Screen.Doctors> {
            DoctorsScreen(
                onDoctorClicked = { doctorId ->
                    navigate(
                        NavigationType.Standard(Screen.DoctorDetails(doctorId = doctorId))
                    )
                }
            )
        }

        composable<Screen.DoctorDetails> {
            val args = it.toRoute<Screen.DoctorDetails>()
            DoctorDetailsScreen(
                doctorId = args.doctorId,
                navigateBack = { navigate(NavigationType.Back) }
            )
        }

        composable<Screen.PatientProfile> {
            ProfileScreen(
                navigateBack = { navigate(NavigationType.Back) }
            )
        }
    }
}