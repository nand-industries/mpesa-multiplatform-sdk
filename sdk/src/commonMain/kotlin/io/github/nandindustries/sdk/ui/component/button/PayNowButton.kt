package io.github.nandindustries.sdk.ui.component.button

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
internal fun PayNowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var widthPx by remember { mutableStateOf(0f) }
    val bandWidth = widthPx / 3f
    val transition = rememberInfiniteTransition()
    val shimmerOffsetX by transition.animateFloat(
        initialValue = -bandWidth,
        targetValue = widthPx + bandWidth,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        )
    )
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0f),
            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0f)
        ),
        start = Offset(shimmerOffsetX, 0f),
        end = Offset(shimmerOffsetX + bandWidth, 0f),
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                widthPx = it.size.width.toFloat()
            }
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    if (enabled && widthPx > 0f) {
                        drawRect(shimmerBrush, blendMode = BlendMode.SrcOver)
                    }
                }
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
        )
    ) {
        Text(text)
    }
}
