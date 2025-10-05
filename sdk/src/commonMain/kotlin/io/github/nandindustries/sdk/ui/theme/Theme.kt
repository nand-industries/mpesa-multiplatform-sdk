package io.github.nandindustries.sdk.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MpesaRedLight,
    onPrimary = Color.Black,
    primaryContainer = MpesaRed,
    onPrimaryContainer = Color.White,
    secondary = MpesaRedDark,
    onSecondary = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = MpesaRed,
    onPrimary = Color.White,
    primaryContainer = MpesaRedLight,
    onPrimaryContainer = Color.Black,
    secondary = MpesaRedDark,
    onSecondary = Color.White,
)

private val Typography = Typography()

@Composable
fun MpesaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme? = null,
    content: @Composable () -> Unit
) {
    val scheme = colorScheme ?: if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = scheme,
        typography = Typography,
        content = content,
    )
}
