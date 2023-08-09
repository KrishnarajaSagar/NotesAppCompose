package com.example.learning3.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.learning3.StoreThemePrefs
import com.example.learning3.ui.theme.themes.BlueDarkColors
import com.example.learning3.ui.theme.themes.BlueLightColors
import com.example.learning3.ui.theme.themes.BrownDarkColors
import com.example.learning3.ui.theme.themes.BrownLightColors
import com.example.learning3.ui.theme.themes.GreenDarkColors
import com.example.learning3.ui.theme.themes.GreenLightColors
import com.example.learning3.ui.theme.themes.PinkDarkColors
import com.example.learning3.ui.theme.themes.PinkLightColors
import com.example.learning3.ui.theme.themes.PurpleDarkColors
import com.example.learning3.ui.theme.themes.PurpleLightColors

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Learning3Theme(
    // darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val dataStore = StoreThemePrefs(context)
    val dynamicColor = dataStore.isDynamicThemeEnabled.collectAsState(initial = true)
    val themeMode = dataStore.getThemeMode.collectAsState(initial = "AUTO")
    val darkTheme = isSystemInDarkTheme()
    val color = dataStore.getColor.collectAsState(initial = "BROWN")

    val colorScheme = when {
        dynamicColor.value && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            // val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        themeMode.value == "AUTO" && darkTheme && color.value == "BROWN" -> BrownDarkColors
        themeMode.value == "AUTO" && darkTheme && color.value == "GREEN" -> GreenDarkColors
        themeMode.value == "AUTO" && darkTheme && color.value == "BLUE" -> BlueDarkColors
        themeMode.value == "AUTO" && darkTheme && color.value == "PURPLE" -> PurpleDarkColors
        themeMode.value == "AUTO" && darkTheme && color.value == "PINK" -> PinkDarkColors

        themeMode.value == "AUTO" && !darkTheme && color.value == "BROWN" -> BrownLightColors
        themeMode.value == "AUTO" && !darkTheme && color.value == "GREEN" -> GreenLightColors
        themeMode.value == "AUTO" && !darkTheme && color.value == "BLUE" -> BlueLightColors
        themeMode.value == "AUTO" && !darkTheme && color.value == "PURPLE" -> PurpleLightColors
        themeMode.value == "AUTO" && !darkTheme && color.value == "PINK" -> PinkLightColors
        themeMode.value == "AUTO" && !darkTheme -> PinkLightColors

        themeMode.value == "DARK" && color.value == "BROWN" -> BrownDarkColors
        themeMode.value == "DARK" && color.value == "GREEN" -> GreenDarkColors
        themeMode.value == "DARK" && color.value == "BLUE" -> BlueDarkColors
        themeMode.value == "DARK" && color.value == "PURPLE" -> PurpleDarkColors
        themeMode.value == "DARK" && color.value == "PINK" -> PinkDarkColors

        themeMode.value == "LIGHT" && color.value == "BROWN" -> BrownLightColors
        themeMode.value == "LIGHT" && color.value == "GREEN" -> GreenLightColors
        themeMode.value == "LIGHT" && color.value == "BLUE" -> BlueLightColors
        themeMode.value == "LIGHT" && color.value == "PURPLE" -> PurpleLightColors
        themeMode.value == "LIGHT" && color.value == "PINK" -> PinkLightColors

        else -> BrownLightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}