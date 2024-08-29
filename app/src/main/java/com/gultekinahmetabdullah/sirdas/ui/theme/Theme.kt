package com.gultekinahmetabdullah.sirdas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
//    primary = darkColor1,
//    secondary = darkColor4,
//    tertiary = darkColor3,
//    background = darkColor1,
//    surface = darkColor4,
//    onPrimary = darkColor2,
//    onSecondary = darkColor3,
//    onTertiary = darkColor4,
//    onBackground = darkColor2,
//    onSurface = darkColor1,

)

private val LightColorScheme = lightColorScheme(
//    primary = lightColor4,
//    onPrimary = lightColor7,
//    primaryContainer = lightColor1,
//    onPrimaryContainer = lightColor1,
//    inversePrimary = lightColor4,
//    secondary = lightColor2,
//    onSecondary = lightColor7,
//    secondaryContainer = lightColor4,
//    onSecondaryContainer = lightColor4,
//    tertiary = lightColor3,
//    onTertiary = lightColor8,
//    tertiaryContainer = lightColor4,
//    onTertiaryContainer = lightColor4,
//    background = lightColor5,
//    onBackground = lightColor1,
//    surface = lightColor6,
//    onSurface = lightColor10,
//    surfaceVariant = lightColor4,
//    onSurfaceVariant = lightColor4,
//    surfaceTint = lightColor4,
//    inverseSurface = lightColor4,
//    inverseOnSurface = lightColor4,
//    error = lightColor4,
//    onError = lightColor4,
//    errorContainer = lightColor4,
//    onErrorContainer = lightColor4,
//    outline = lightColor1,
//    outlineVariant = lightColor4,
//    scrim = lightColor4,
//    surfaceBright = lightColor4,
//    surfaceContainer = lightColor4,
//    surfaceContainerHigh = lightColor4,
//    surfaceContainerHighest = lightColor4,
//    surfaceContainerLow = lightColor4,
//    surfaceContainerLowest = lightColor4,
//    surfaceDim = lightColor4,

)

private val thirdColorScheme = lightColorScheme(
//    primary = lightColor4,
//    onPrimary = lightColor7,
//    primaryContainer = lightColor1,
//    onPrimaryContainer = lightColor1,
//    inversePrimary = lightColor4,
//    secondary = lightColor2,
)

@Composable
fun SirdasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        /*
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
         */

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}