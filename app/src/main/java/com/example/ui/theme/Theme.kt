package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NeoNoirDarkColorScheme = darkColorScheme(
    primary = NeonGold,
    onPrimary = SlateDarkBackground,
    primaryContainer = SlateCardSurfaceElevated,
    onPrimaryContainer = NeonGoldLight,
    secondary = CyberCyan,
    onSecondary = SlateDarkBackground,
    secondaryContainer = SlateBorder,
    onSecondaryContainer = CyberCyanLight,
    tertiary = EmeraldPaid,
    background = SlateDarkBackground,
    onBackground = TextPrimary,
    surface = SlateCardSurface,
    onSurface = TextPrimary,
    surfaceVariant = SlateCardSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = SlateBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NeoNoirDarkColorScheme,
        typography = Typography,
        content = content
    )
}
