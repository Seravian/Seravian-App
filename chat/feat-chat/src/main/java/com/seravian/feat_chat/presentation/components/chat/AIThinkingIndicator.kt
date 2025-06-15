package com.seravian.feat_chat.presentation.components.chat

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.feat_chat.R

@Composable
fun AITypingIndicator(
    modifier: Modifier = Modifier,
    dotColor: Color = MaterialTheme.colorScheme.secondary,
    dotSize: Dp = 8.dp,
    animationDelay: Int = 200
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // Chatbot icon container
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.chatbot_ic),
                modifier = Modifier.size(40.dp)
            )
        }

        // Typing dots with improved sequential animation
        val infiniteTransition = rememberInfiniteTransition()

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(40.dp)
                .width(80.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Three dots with staggered animation
                (0..2).forEach { index ->
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.5f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = animationDelay * 4 // Total cycle time
                                1.0f at 0
                                1.0f at index * animationDelay
                                1.5f at (index * animationDelay) + animationDelay with FastOutSlowInEasing
                                1.0f at (index * animationDelay) + (animationDelay * 2)
                                1.0f at animationDelay * 4
                            },
                            repeatMode = RepeatMode.Restart
                        )
                    )

                    Canvas(
                        modifier = Modifier.size(dotSize * scale)
                    ) {
                        drawCircle(
                            color = dotColor.copy(
                                alpha = when {
                                    // Fade in/out logic
                                    scale > 1.25f -> 1f
                                    scale > 1.1f -> 0.8f
                                    else -> 0.4f
                                }
                            ),
                            radius = size.minDimension / 2
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PulsingCirclePreview() {
    AppTheme {
        AITypingIndicator(
            modifier = Modifier.padding(16.dp)
        )
    }
}