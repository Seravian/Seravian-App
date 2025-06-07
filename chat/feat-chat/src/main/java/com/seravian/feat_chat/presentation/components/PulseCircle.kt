package com.seravian.feat_chat.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.feat_chat.R
import kotlinx.coroutines.yield
import kotlin.math.sqrt

@Composable
fun PulseCircle(
    amplitude: Float,
    icon: Painter,
    isThinking: Boolean,
    modifier: Modifier = Modifier
) {
    val normalizedAmp = (amplitude / 32767f).coerceIn(0f, 1f)
    val scaleFactor = sqrt(normalizedAmp).coerceIn(0f, 1f)

    val baseSize = 110.dp
    val targetSize by animateDpAsState(
        targetValue = baseSize * (1f + (scaleFactor * 1.1f)),
        animationSpec = tween(durationMillis = 50)
    )

    val colorScheme = MaterialTheme.colorScheme
    val rippleAnimatable = remember { Animatable(0f) }

    // Calculate the full animation duration including the fade-out of all circles
    // The last circle starts at 0.5f (2 * 0.25f) and needs to complete its full cycle (1.0)
    // So we need to animate to 1.5f instead of 1.0f to ensure all circles finish
    LaunchedEffect(isThinking) {
        if (isThinking) {
            // Continuous animation loop while in "thinking" state
            while (true) {
                // Animate to 1.5f to allow the last ripple (starting at delay 0.5f) to complete
                rippleAnimatable.animateTo(
                    targetValue = 1.5f,
                    animationSpec = tween(durationMillis = 2250, easing = LinearEasing)
                )
                rippleAnimatable.snapTo(0f) // Reset animation
                yield() // Allow composition to happen
            }
        } else {
            // If not thinking, just reset the animation
            rippleAnimatable.snapTo(0f)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(baseSize * 2.5f) // Larger container to accommodate ripples
    ) {
        // Draw multiple ripple circles when in thinking state
        if (isThinking) {
            for (i in 0 until 3) {
                val rippleDelay = i * 0.25f
                val adjustedProgress = (rippleAnimatable.value - rippleDelay).coerceIn(0f, 1f)

                if (adjustedProgress > 0 && adjustedProgress < 1f) { // Only draw when visible
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Size grows as animation progresses
                        val rippleSize = targetSize.toPx() * (1f + adjustedProgress * 1.5f)

                        // Opacity fades out as ripple expands
                        val alpha = (1f - adjustedProgress) * 0.4f

                        drawCircle(
                            color = colorScheme.secondaryContainer.copy(alpha = alpha),
                            radius = rippleSize / 2f,
                            center = center,
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }
        }

        // The pulsing icon
        Image(
            painter = icon,
            contentDescription = "Pulsing Icon",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(targetSize)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PulseCirclePreview() {
    AppTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Preview in idle state
            PulseCircle(
                amplitude = 20000f,
                icon = painterResource(id = R.drawable.logo),
                isThinking = false
            )

            // Preview in thinking state
            PulseCircle(
                amplitude = 20000f,
                icon = painterResource(id = R.drawable.logo),
                isThinking = true
            )
        }
    }
}