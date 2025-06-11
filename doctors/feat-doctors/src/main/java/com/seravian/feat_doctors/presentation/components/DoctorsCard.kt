package com.seravian.feat_doctors.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.seravian.feat_doctors.R
import com.seravian.feat_doctors.presentation.model.DoctorUI


@Composable
fun DoctorCard(
    doctorUI: DoctorUI,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
           doctorUI.profileImageUrl?.let {
               AsyncImage(
                   model = ImageRequest.Builder(LocalContext.current)
                       .data(doctorUI.profileImageUrl ?: "")
                       .crossfade(true)
                       .build(),
                   placeholder = painterResource(R.drawable.person_ic),
                   contentDescription = stringResource(R.string.description),
                   contentScale = ContentScale.Crop,
                   modifier = Modifier
                       .width(100.dp)
                       .height(100.dp)
                       .clip(RoundedCornerShape(10.dp)),
               )
           }
            if(doctorUI.profileImageUrl==null){
                Image(
                    painter = painterResource(id = R.drawable.person_ic),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column() {
                Text(
                    text = doctorUI.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = doctorUI.salary+" EGP",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = doctorUI.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DoctorPreview() {
//    DoctorCard(
//        imageRes = com.greenvenom.core_ui.R.drawable.logo,
//        name = "kareem Essam",
//        salary = "20$",
//        description = "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk akkkkkkkkkkk",
//        onClick = {}
//    )
}
