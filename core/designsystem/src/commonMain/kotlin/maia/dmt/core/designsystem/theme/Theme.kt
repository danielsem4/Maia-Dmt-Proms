package maia.dmt.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val ColorScheme.extended: ExtendedColors
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColors.current

@Immutable
data class ExtendedColors(
    // Button states
    val primaryHover: Color,
    val destructiveHover: Color,
    val destructiveSecondaryOutline: Color,
    val disabledOutline: Color,
    val disabledFill: Color,
    val successOutline: Color,
    val success: Color,
    val onSuccess: Color,
    val secondaryFill: Color,

    // Text variants
    val textPrimary: Color,
    val textTertiary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Surface variants
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val overlay: Color,

    // Accent colors
    val accentBlue: Color,
    val accentPurple: Color,
    val accentViolet: Color,
    val accentPink: Color,
    val accentOrange: Color,
    val accentYellow: Color,
    val accentGreen: Color,
    val accentTeal: Color,
    val accentLightBlue: Color,
    val accentGrey: Color,

    // Cake colors for chat bubbles
    val cakeViolet: Color,
    val cakeGreen: Color,
    val cakeBlue: Color,
    val cakePink: Color,
    val cakeOrange: Color,
    val cakeYellow: Color,
    val cakeTeal: Color,
    val cakePurple: Color,
    val cakeRed: Color,
    val cakeMint: Color,
)

val LightExtendedColors = ExtendedColors(
    primaryHover = DmtBrand600,
    destructiveHover = DmtRed600,
    destructiveSecondaryOutline = DmtRed200,
    disabledOutline = DmtBase200,
    disabledFill = DmtBase150,
    successOutline = DmtBrand100,
    success = DmtBrand600,
    onSuccess = DmtBase0,
    secondaryFill = DmtBase100,

    textPrimary = DmtBase1000,
    textTertiary = DmtBase800,
    textSecondary = DmtBase900,
    textPlaceholder = DmtBase700,
    textDisabled = DmtBase400,

    surfaceLower = DmtBase100,
    surfaceHigher = DmtBase100,
    surfaceOutline = DmtBase1000Alpha14,
    overlay = DmtBase1000Alpha80,

    accentBlue = DmtBlue,
    accentPurple = DmtPurple,
    accentViolet = DmtViolet,
    accentPink = DmtPink,
    accentOrange = DmtOrange,
    accentYellow = DmtYellow,
    accentGreen = DmtGreen,
    accentTeal = DmtTeal,
    accentLightBlue = DmtLightBlue,
    accentGrey = DmtGrey,

    cakeViolet = DmtCakeLightViolet,
    cakeGreen = DmtCakeLightGreen,
    cakeBlue = DmtCakeLightBlue,
    cakePink = DmtCakeLightPink,
    cakeOrange = DmtCakeLightOrange,
    cakeYellow = DmtCakeLightYellow,
    cakeTeal = DmtCakeLightTeal,
    cakePurple = DmtCakeLightPurple,
    cakeRed = DmtCakeLightRed,
    cakeMint = DmtCakeLightMint,
)

val DarkExtendedColors = ExtendedColors(
    primaryHover = DmtBrand600,
    destructiveHover = DmtRed600,
    destructiveSecondaryOutline = DmtRed200,
    disabledOutline = DmtBase900,
    disabledFill = DmtBase1000,
    successOutline = DmtBrand500Alpha40,
    success = DmtBrand500,
    onSuccess = DmtBase1000,
    secondaryFill = DmtBase900,

    textPrimary = DmtBase0,
    textTertiary = DmtBase200,
    textSecondary = DmtBase150,
    textPlaceholder = DmtBase400,
    textDisabled = DmtBase500,

    surfaceLower = DmtBase1000,
    surfaceHigher = DmtBase900,
    surfaceOutline = DmtBase100Alpha10Alt,
    overlay = DmtBase1000Alpha80,

    accentBlue = DmtBlue,
    accentPurple = DmtPurple,
    accentViolet = DmtViolet,
    accentPink = DmtPink,
    accentOrange = DmtOrange,
    accentYellow = DmtYellow,
    accentGreen = DmtGreen,
    accentTeal = DmtTeal,
    accentLightBlue = DmtLightBlue,
    accentGrey = DmtGrey,

    cakeViolet = DmtCakeDarkViolet,
    cakeGreen = DmtCakeDarkGreen,
    cakeBlue = DmtCakeDarkBlue,
    cakePink = DmtCakeDarkPink,
    cakeOrange = DmtCakeDarkOrange,
    cakeYellow = DmtCakeDarkYellow,
    cakeTeal = DmtCakeDarkTeal,
    cakePurple = DmtCakeDarkPurple,
    cakeRed = DmtCakeDarkRed,
    cakeMint = DmtCakeDarkMint,
)

val LightColorScheme = lightColorScheme(
    primary = DmtBrand500,
    onPrimary = DmtBrand1000,
    primaryContainer = DmtBrand100,
    onPrimaryContainer = DmtBrand900,

    secondary = DmtBase700,
    onSecondary = DmtBase0,
    secondaryContainer = DmtBase100,
    onSecondaryContainer = DmtBase900,

    tertiary = DmtBrand900,
    onTertiary = DmtBase0,
    tertiaryContainer = DmtBrand100,
    onTertiaryContainer = DmtBrand1000,

    error = DmtRed500,
    onError = DmtBase0,
    errorContainer = DmtRed200,
    onErrorContainer = DmtRed600,

    background = DmtBase100, // Your backgroundColor #f0f9ff
    onBackground = DmtBase1000,
    surface = DmtBase0, // Your Surface color #FFFFFF
    onSurface = DmtBase1000,
    surfaceVariant = DmtBase100,
    onSurfaceVariant = DmtBase900,

    outline = DmtBase1000Alpha8,
    outlineVariant = DmtBase200,
)

val DarkColorScheme = darkColorScheme(
    primary = DmtBrand500,
    onPrimary = DmtBrand1000,
    primaryContainer = DmtBrand900,
    onPrimaryContainer = DmtBrand500,

    secondary = DmtBase400,
    onSecondary = DmtBase1000,
    secondaryContainer = DmtBase900,
    onSecondaryContainer = DmtBase150,

    tertiary = DmtBrand500,
    onTertiary = DmtBase1000,
    tertiaryContainer = DmtBrand900,
    onTertiaryContainer = DmtBrand500,

    error = DmtRed500,
    onError = DmtBase0,
    errorContainer = DmtRed600,
    onErrorContainer = DmtRed200,

    background = DmtBase1000, // Dark background
    onBackground = DmtBase0,
    surface = DmtBase950, // Dark surface
    onSurface = DmtBase0,
    surfaceVariant = DmtBase900,
    onSurfaceVariant = DmtBase150,

    outline = DmtBase100Alpha10,
    outlineVariant = DmtBase800,
)