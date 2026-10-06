package com.autoformation.pharmaciesdegardes.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Schéma de couleurs CLAIR ───────────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    // Primaire : Vert émeraude
    primary          = Emerald50,
    onPrimary        = Color.White,
    primaryContainer = Emerald90,
    onPrimaryContainer = Emerald60,

    // Secondaire : Vert clair (variante)
    secondary          = Emerald40,
    onSecondary        = Color.White,
    secondaryContainer = Emerald10,
    onSecondaryContainer = Emerald60,

    // Tertiaire : Orange/Ambre (accent)
    tertiary          = Amber40,
    onTertiary        = Color.White,
    tertiaryContainer = Amber90,
    onTertiaryContainer = Amber40,

    // Surfaces / Fonds
    background        = GrayNeutral10,
    onBackground      = GrayNeutral90,
    surface           = Color.White,
    onSurface         = GrayNeutral90,
    surfaceVariant    = GrayNeutral20,
    onSurfaceVariant  = GrayNeutral70,

    // Erreur
    error             = ErrorRed,
    onError           = Color.White,
    errorContainer    = ErrorRedLight,
    onErrorContainer  = ErrorRed,

    // Outline
    outline           = GrayNeutral40
)

// ── Schéma de couleurs SOMBRE ──────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    // Primaire
    primary          = Emerald80,
    onPrimary        = Emerald60,
    primaryContainer = Emerald60,
    onPrimaryContainer = Emerald10,

    // Secondaire
    secondary          = Emerald80,
    onSecondary        = Emerald60,
    secondaryContainer = DarkSurface3,
    onSecondaryContainer = Emerald80,

    // Tertiaire
    tertiary          = Amber80,
    onTertiary        = Amber40,
    tertiaryContainer = DarkSurface3,
    onTertiaryContainer = Amber80,

    // Surfaces / Fonds
    background        = DarkSurface,
    onBackground      = Color(0xFFDCE8E3),
    surface           = DarkSurface2,
    onSurface         = Color(0xFFDCE8E3),
    surfaceVariant    = DarkSurface3,
    onSurfaceVariant  = GrayNeutral40,

    // Erreur
    error             = Color(0xFFFF8A80),
    onError           = Color(0xFF690000),
    errorContainer    = Color(0xFF930000),
    onErrorContainer  = Color(0xFFFFDAD6),

    // Outline
    outline           = GrayNeutral70
)

// ── Thème principal ────────────────────────────────────────────────────
@Composable
fun PharmaciesDeGardesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Couleur de la barre de statut système adaptée au thème
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}