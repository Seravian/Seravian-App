package com.seravian.feat_doctors.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seravian.core_ui.components.TopAppBar
import com.seravian.core_ui.presentation.BaseAction
import com.seravian.core_ui.presentation.BaseScreen
import com.seravian.feat_doctors.presentation.components.DoctorCard
import com.seravian.feat_doctors.presentation.model.toDoctorUI
import com.seravian.feat_doctors.presentation.viewModel.doctor.DoctorAction
import com.seravian.feat_doctors.presentation.viewModel.doctor.DoctorState
import com.seravian.feat_doctors.presentation.viewModel.doctor.DoctorViewModel

@Composable
fun DoctorsScreen(
    onDoctorClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BaseScreen<DoctorViewModel> { viewModel ->
        val state by viewModel.doctorState.collectAsStateWithLifecycle()

        Scaffold(
            topBar = {
                TopAppBar(
                    isVisible = true,
                    isActionEnabled = true,
                    isSideDestination = false,
                    title = "Doctors"
                )
            }
        ) { innerPadding ->
            DoctorsList(
                doctorState = state,
                doctorAction = {
                    when (it) {
                        is DoctorAction.OnDoctorClick -> onDoctorClicked(it.doctor.doctorId)
                        else -> {}
                    }
                    viewModel.doctorAction(it)
                },
                baseAction = viewModel::baseAction,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun DoctorsList(
    doctorState: DoctorState,
    doctorAction: (DoctorAction) -> Unit,
    baseAction: (BaseAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(doctorState.doctors) { doctor ->
            DoctorCard(
                doctorUI = doctor.toDoctorUI(),
                onClick = { doctorAction(DoctorAction.OnDoctorClick(doctor)) }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDoctorsScreen() {
//    DoctorsList(
//        doctorState = DoctorState(
//            doctors = listOf(
//                Doctor("1", com.greenvenom.core_ui.R.drawable.logo, "Kareem", "$20", "Specialist")
//            )
//        ),
//        doctorAction = {},
//        baseAction = {}
//    )
}
