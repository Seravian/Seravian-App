package com.seravian.feat_doctors.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.seravian.core_ui.components.TopAppBar
import com.seravian.core_ui.presentation.BaseAction
import com.seravian.core_ui.presentation.BaseScreen
import com.seravian.feat_doctors.presentation.viewModel.detailsDoctor.DoctorDetailsAction
import com.seravian.feat_doctors.presentation.viewModel.detailsDoctor.DoctorDetailsState
import com.seravian.feat_doctors.presentation.viewModel.detailsDoctor.DoctorDetailsViewModel
import com.seravian.feat_doctors.R.string.doctors
import com.seravian.feat_doctors.presentation.components.BookingDialog

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
        var showBookingDialog by remember { mutableStateOf(false) }

        if (showBookingDialog) {
            BookingDialog(
                doctorId = doctorDetailsState.doctor?.doctorId.orEmpty(),
                onDismiss = { showBookingDialog = false },
                onBook = { fromUtc, toUtc ->
                    doctorDetailsAction(DoctorDetailsAction.BookSession(
                        patientIsAvailableToUtc = toUtc,
                        patientIsAvailableFromUtc = fromUtc,
                        patientNote = ""
                    ))
                    showBookingDialog = false
                }
            )
        }

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
                    doctorDetailsState.doctor?.doctorImageUrl?.let {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(doctorDetailsState.doctor.doctorImageUrl ?: "")
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(com.seravian.feat_doctors.R.drawable.person_ic),
                            contentDescription = stringResource(com.seravian.feat_doctors.R.string.description),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)),
                        )
                    }
                    if(doctorDetailsState.doctor?.doctorImageUrl==null){
                        Image(
                            painter = painterResource(id = com.seravian.feat_doctors.R.drawable.doctor_ic),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = doctorDetailsState.doctor?.doctorFullName?:"",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = (doctorDetailsState.doctor?.doctorSessionPrice.toString() + " EGP")
                            ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = doctorDetailsState.doctor?.doctorDescription?:"",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { showBookingDialog = true  },
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
//    val sampleDoctor = Doctor(
//        doctorId = "1",
//        doctorImageUrl = R.drawable.logo,
//        name = "Dr. Kareem Essam",
//        salary = "$50/hr",
//        description = "Specialist in mental health with 10+ years of experience in psychotherapy and counseling."
//    )
//    DoctorDetailsContent(doctorDetailsState = DoctorDetailsState(sampleDoctor), doctorDetailsAction = {}, baseAction = {})
}
