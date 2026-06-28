package com.planes.android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

val md_theme_light_primary = Aqua
val md_theme_light_onPrimary = Black
val md_theme_light_primaryContainer = colorLightBlue100
val md_theme_light_onPrimaryContainer = Black

val md_theme_light_secondary = VideoPressedColor
val md_theme_light_onSecondary = Black
val md_theme_light_secondaryContainer = colorGreen100
val md_theme_light_onSecondaryContainer = Black


val md_theme_light_tertiary = colorBlue500
val md_theme_light_onTertiary = White
val md_theme_light_tertiaryContainer = colorBlue100
val md_theme_light_onTertiaryContainer = Black


val md_theme_light_error = colorRed500
val md_theme_light_onError = White
val md_theme_light_errorContainer = colorRed100
val md_theme_light_onErrorContainer = Black


val md_theme_light_background = LayoutBackgroundStartColor
val md_theme_light_onBackground = Black

val md_theme_light_surface = White
val md_theme_light_onSurface = Black


val md_theme_light_surfaceVariant = colorGray200
val md_theme_light_onSurfaceVariant = colorGray800
val md_theme_light_outline = colorGray600
val md_theme_light_outlineVariant = colorGray400


val md_theme_light_inverseSurface = colorGray900
val md_theme_light_inverseOnSurface = White
val md_theme_light_inversePrimary = colorBlue700

val md_theme_light_surfaceTint = md_theme_light_primary
val md_theme_light_scrim = Black.copy(alpha = 0.32f)



private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)

val md_theme_dark_primary = Aqua
val md_theme_dark_onPrimary = Black
val md_theme_dark_primaryContainer = colorLightBlue700
val md_theme_dark_onPrimaryContainer = Black


val md_theme_dark_secondary = VideoPressedColor
val md_theme_dark_onSecondary = Black
val md_theme_dark_secondaryContainer = colorGreen700
val md_theme_dark_onSecondaryContainer = Black


val md_theme_dark_tertiary = colorBlue300
val md_theme_dark_onTertiary = Black
val md_theme_dark_tertiaryContainer = colorBlue700
val md_theme_dark_onTertiaryContainer = White


val md_theme_dark_error = colorRed300
val md_theme_dark_onError = Black
val md_theme_dark_errorContainer = colorRed700
val md_theme_dark_onErrorContainer = White


val md_theme_dark_background = LayoutBackgroundEndColor
val md_theme_dark_onBackground = White

val md_theme_dark_surface = colorGray900
val md_theme_dark_onSurface = White


val md_theme_dark_surfaceVariant = colorGray700
val md_theme_dark_onSurfaceVariant = colorGray200
val md_theme_dark_outline = colorGray400
val md_theme_dark_outlineVariant = colorGray600


val md_theme_dark_inverseSurface = colorGray100
val md_theme_dark_inverseOnSurface = Black
val md_theme_dark_inversePrimary = colorBlue300


val md_theme_dark_surfaceTint = md_theme_dark_primary
val md_theme_dark_scrim = Black.copy(alpha = 0.32f)



private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)


@Composable
fun PlanesComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}