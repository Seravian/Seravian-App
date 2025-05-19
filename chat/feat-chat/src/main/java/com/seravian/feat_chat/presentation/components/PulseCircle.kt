package com.seravian.feat_chat.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.feat_chat.R
import kotlin.math.sqrt

@Composable
fun PulseCircle(
    amplitude: Float, // Expected range between 0-32767 for 16-bit audio
    icon: Painter,
    modifier: Modifier = Modifier
) {
    // Normalize and apply non-linear curve as before
    val normalizedAmp = (amplitude / 32767f).coerceIn(0f, 1f)
    val scaleFactor = sqrt(normalizedAmp).coerceIn(0f, 1f)

    // Calculate the target size (15% max growth on top of base size)
    val baseSize = 130.dp
    val targetSize by animateDpAsState(
        targetValue = baseSize * (1f + (scaleFactor * 0.6f)),
        animationSpec = tween(durationMillis = 50)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Image(
            painter = icon,
            contentDescription = "Pulsing Icon",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(targetSize) // Animate the size directly
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PulseCirclePreview() {
    AppTheme {
        PulseCircle(
            amplitude = 20000f,
            icon = painterResource(id = R.drawable.logo)
        )
    }
}