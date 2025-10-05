package io.github.nandindustries.sdk.ui.component.counter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
internal fun TimeoutCountdown(
    start: Int,
    modifier: Modifier = Modifier,
    onFinished: () -> Unit = {}
) {
    var number by rememberSaveable(start) { mutableStateOf(start) }
    val onFinishedState by rememberUpdatedState(onFinished)

    LaunchedEffect(start) {
        while (isActive && number > 0) {
            delay(1000)
            number--
        }
        if (number == 0) onFinishedState()
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        AnimatedContent(
            targetState = number,
            transitionSpec = {
                val duration = 320
                (slideInVertically(
                    animationSpec = tween(duration, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 2 }
                ) + fadeIn(tween(duration / 2))) togetherWith
                        (slideOutVertically(
                            animationSpec = tween(duration, easing = FastOutSlowInEasing),
                            targetOffsetY = { -it / 2 },
                        ) + fadeOut(tween(duration / 2))) using
                        SizeTransform(clip = false)
            },
            label = "countdown"
        ) { value ->
            Text(
                text = value.toString(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1).sp,
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
