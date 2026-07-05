package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFF94A3B8), // slate-400
    tertiary = Color(0xFF81C784), // Success light
    background = Color(0xFF0F172A), // slate-900
    surface = Color(0xFF1E293B), // slate-800
    onPrimary = Color(0xFF381E72),
    onSecondary = Color(0xFFF1F5F9), // slate-100
    onTertiary = Color(0xFF003314),
    onBackground = Color(0xFFF8FAFC), // slate-50
    onSurface = Color(0xFFF8FAFC),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    outlineVariant = Color(0xFF334155) // slate-700
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PulsePrimary,
    secondary = PulseTextSecondary,
    tertiary = PulseSuccess,
    background = PulseBackground,
    surface = CardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = PulseTextPrimary,
    onSurface = PulseTextPrimary,
    primaryContainer = PulsePrimaryContainer,
    onPrimaryContainer = PulseOnPrimaryContainer,
    outlineVariant = CardBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
