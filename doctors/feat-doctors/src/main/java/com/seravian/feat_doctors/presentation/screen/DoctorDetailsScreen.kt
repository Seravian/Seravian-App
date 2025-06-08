//package com.seravian.feat_doctors.presentation.screen
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.seravian.feat_doctors.presentation.viewModel.doctor.DoctorViewModel
//
//@SuppressLint("StateFlowValueCalledInComposition")
//@Composable
//fun DoctorDetailsScreen(doctorId: String, viewModel: DoctorViewModel = viewModel()) {
//    val doctor = viewModel.doctorState.value.doctors.find { it.id == doctorId }
//    doctor?.let {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = it.name, style = MaterialTheme.typography.headlineMedium)
//            Text(text = it.salary, color = Color.Gray)
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = it.description)
//        }
//    }
//}
//
//@Preview()
//@Composable
//fun DoctorDetailsScreenPreview(modifier: Modifier = Modifier) {
//    DoctorDetailsScreen("")
//}
package com.seravian.feat_doctors.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.R
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseScreen
import com.seravian.feat_doctors.presentation.model.Doctor
import com.seravian.feat_doctors.presentation.viewModel.detailsDoctor.DoctorDetailsAction
import com.seravian.feat_doctors.presentation.viewModel.detailsDoctor.DoctorDetailsState
import com.seravian.feat_doctors.presentation.viewModel.detailsDoctor.DoctorDetailsViewModel
import com.seravian.feat_doctors.R.string.doctors
@Composable
fun DoctorDetailsScreen(
    doctorId: String,
    navigateBack: () -> Unit
) {
    BaseScreen<DoctorDetailsViewModel>(
        onPhysicalBack = {viewModel ->
            viewModel.doctorDetailsAction(DoctorDetailsAction.BackToDoctorsList)
            navigateBack()
        }
    ) { viewModel ->
        val state by viewModel.doctorDetailsState.collectAsStateWithLifecycle()
        DoctorDetailsContent(
            doctorDetailsState = state,
            doctorDetailsAction = {
                when (it) {
                    is DoctorDetailsAction.BackToDoctorsList -> navigateBack()
                    else -> {}
                }
                viewModel.doctorDetailsAction(it)
            },
            baseAction = viewModel::baseAction
        )
    }
}

@Composable
fun DoctorDetailsContent(
    doctorDetailsState: DoctorDetailsState,
    doctorDetailsAction: (DoctorDetailsAction) -> Unit,
    baseAction: (BaseAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                isVisible = true,
                isActionEnabled = true,
                isSideDestination = true,
                title = stringResource(doctors),
                navigateBack = {doctorDetailsAction(DoctorDetailsAction.BackToDoctorsList)}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = doctorDetailsState.doctor?.imageRes?:1),
                        contentDescription = "Doctor Image",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = doctorDetailsState.doctor?.name?:"",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = doctorDetailsState.doctor?.salary?:"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = doctorDetailsState.doctor?.description?:"",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { /* TODO: Navigate to booking or action */ },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Book Now")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorDetailsScreenPreview() {
    val sampleDoctor = Doctor(
        id = "1",
        imageRes = R.drawable.logo,
        name = "Dr. Kareem Essam",
        salary = "$50/hr",
        description = "Specialist in mental health with 10+ years of experience in psychotherapy and counseling."
    )
    DoctorDetailsContent(doctorDetailsState = DoctorDetailsState(sampleDoctor), doctorDetailsAction = {}, baseAction = {})
}
