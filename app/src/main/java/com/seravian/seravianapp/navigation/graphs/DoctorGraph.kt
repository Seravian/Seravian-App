package com.seravian.seravianapp.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.greenvenom.core_navigation.data.NavigationType
import com.seravian.feat_navigation.routes.Screen
import com.seravian.feat_navigation.routes.SubGraph
import com.seravian.feat_profile.presentation.screen.ProfileScreen
import com.seravian.feat_verification.presentation.screens.RequestDetailsScreen
import com.seravian.feat_verification.presentation.screens.VerificationRequestsScreen

fun NavGraphBuilder.doctorGraph(navigate: (NavigationType) -> Unit) {
    navigation<SubGraph.Doctor>(startDestination = Screen.DoctorAppointments) {
        composable<Screen.DoctorVerifications> {
            VerificationRequestsScreen(
                navigateBack = { navigate(NavigationType.Back) }
            )
        }

        composable<Screen.DoctorVerificationDetails> {
            val args = it.toRoute<Screen.DoctorVerificationDetails>()

            RequestDetailsScreen(
                requestId = args.requestId,
                navigateBack = { navigate(NavigationType.Back) }
            )
        }

        composable<Screen.DoctorAppointments> {

        }

        composable<Screen.AppointmentDetails> {

        }

        composable<Screen.DoctorRequests> {

        }

        composable<Screen.RequestDetails> {

        }

        composable<Screen.DoctorProfile> {
            ProfileScreen()
        }
    }
}