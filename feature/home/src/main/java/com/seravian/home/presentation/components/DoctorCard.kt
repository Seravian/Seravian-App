package com.seravian.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.seravian.home.R
import com.seravian.ui.theme.SeravianTheme

@Composable
fun DoctorCard(
    name: String,
    title: String,
    location: String,
    @DrawableRes image: Int,
    navigateToDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Card (
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = modifier
            .padding(8.dp)
            .width(140.dp)
            .height(180.dp)
            .clickable { navigateToDetails() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        color = colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Image(
                    painter = painterResource(image),
                    contentDescription = "Doctor's Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onBackground
            )
            Text(
                text = location,
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onBackground
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DoctorCardPreview() {
    SeravianTheme {
        DoctorCard(
            name = "Dr. John Doe",
            title = "Cardiologist",
            location = "New York, USA",
            image = R.drawable.doctor_ic,
            navigateToDetails = {}
        )
    }
}