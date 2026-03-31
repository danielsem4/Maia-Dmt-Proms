---
name: kmp-ui-components
description: Creating reusable UI components following DMTProms design system conventions (Dmt prefix, responsive sizing, theme colors)
---

# Creating UI Components in DMTProms

Use this skill when creating a component, widget, or reusable UI element.

## Where to Place Components

**Shared across features** -> `core/designsystem/src/commonMain/kotlin/maia/dmt/core/designsystem/components/<category>/`

Categories that exist:
- `buttons/` — DmtButton, DmtIconButton, DmtToggleButton
- `cards/` — DmtCard, DmtIconCard, DmtParagraphCard, DmtTextCard
- `charts/` — DmtBarChart, DmtLineChart
- `dialogs/` — DmtActionDialog, DmtConfirmationDialog, DmtContentDialog, DmtCustomDialog, DmtDatePickerDialog, DmtInfoDialog, DmtTimePickerDialog, DmtTransparentDialog
- `layouts/` — DmtBaseScreen, DmtAdaptiveFormLayout, DmtSnackBarScaffold, DmtSurface
- `textFields/` — DmtTextField, DmtPasswordTextField, DmtSearchTextField, DmtParagraphTextField, DmtTextFieldLayout
- `select/` — DmtCheckBoxGroup, DmtCheckboxCardGroup, DmtDropDown, DmtRadioButtonGroup, DmtHumanBodyLayout
- `canvas/` — DrawingCanvas, CapturableCanvas, DrawingController
- `toast/` — DmtToastMessage, DmtToastView
- `scale/` — DmtScaleSlider
- `logo/` — DmtLogo
- `animations/` — AnimatedSpeaker
- `capture/` — CapturableBox
- `shapeDraw/` — DmtShape, ShapeDrawer, ShapeRegistry

**Feature-specific** -> `feature/<name>/presentation/src/commonMain/kotlin/maia/dmt/<name>/presentation/components/`

## Check Before Creating

Before creating a new component, check if one of these existing components can be used or extended:
- Need a button? -> `DmtButton` with `DmtButtonStyle` enum (PRIMARY, DESTRUCTIVE_PRIMARY, SECONDARY, DESTRUCTIVE_SECONDARY, TEXT)
- Need a card? -> `DmtCard`, `DmtIconCard`, `DmtTextCard`, `DmtParagraphCard`
- Need a text field? -> `DmtTextField`, `DmtPasswordTextField`, `DmtSearchTextField`
- Need a dialog? -> `DmtConfirmationDialog`, `DmtActionDialog`, `DmtContentDialog`
- Need a layout? -> `DmtBaseScreen` for standard screen layout
- Need a chart? -> `DmtBarChart`, `DmtLineChart`
- Need selection? -> `DmtRadioButtonGroup`, `DmtCheckBoxGroup`, `DmtDropDown`
- Need drawing? -> `DrawingCanvas` with `DrawingController`
- Need a toast? -> `DmtToastMessage` with `ToastType` and `ToastDuration`

## Component Template

```kotlin
package maia.dmt.core.designsystem.components.<category>
// OR: package maia.dmt.<feature>.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.core.presentation.util.DeviceConfiguration
import maia.dmt.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

// Style enum if component has variants
enum class Dmt<Name>Style {
    DEFAULT,
    OUTLINED,
    COMPACT
}

@Composable
fun Dmt<Name>(
    // Required parameters first
    text: String,
    onClick: () -> Unit,
    // Modifier always with default
    modifier: Modifier = Modifier,
    // Style/config with defaults
    style: Dmt<Name>Style = Dmt<Name>Style.DEFAULT,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    // Optional composable slots last
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val configuration = currentDeviceConfiguration()

    // Responsive sizing based on device
    val contentPadding = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 8.dp
        DeviceConfiguration.MOBILE_LANDSCAPE -> 10.dp
        DeviceConfiguration.TABLET_PORTRAIT -> 12.dp
        DeviceConfiguration.TABLET_LANDSCAPE -> 14.dp
        DeviceConfiguration.DESKTOP -> 16.dp
    }

    val textStyle = when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> MaterialTheme.typography.bodyMedium
        DeviceConfiguration.MOBILE_LANDSCAPE -> MaterialTheme.typography.bodyMedium
        DeviceConfiguration.TABLET_PORTRAIT -> MaterialTheme.typography.bodyLarge
        DeviceConfiguration.TABLET_LANDSCAPE -> MaterialTheme.typography.bodyLarge
        DeviceConfiguration.DESKTOP -> MaterialTheme.typography.titleMedium
    }

    // Use theme colors
    val backgroundColor = when (style) {
        Dmt<Name>Style.DEFAULT -> MaterialTheme.colorScheme.surface
        Dmt<Name>Style.OUTLINED -> MaterialTheme.colorScheme.background
        Dmt<Name>Style.COMPACT -> MaterialTheme.colorScheme.surfaceVariant
    }

    // Extended colors for custom design tokens
    val borderColor = MaterialTheme.colorScheme.extended.surfaceOutline
    val disabledColor = MaterialTheme.colorScheme.extended.disabledFill
    val secondaryText = MaterialTheme.colorScheme.extended.textSecondary

    // Component implementation
    Card(
        modifier = modifier.padding(contentPadding),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        // ...
    }
}

// Preview at bottom of file
@Composable
@Preview
fun Dmt<Name>Preview() {
    DmtTheme {
        Dmt<Name>(
            text = "Preview",
            onClick = {}
        )
    }
}

@Composable
@Preview
fun Dmt<Name>DarkPreview() {
    DmtTheme(darkTheme = true) {
        Dmt<Name>(
            text = "Dark Preview",
            onClick = {}
        )
    }
}
```

## Theme & Color Usage

**Standard Material colors:**
```kotlin
MaterialTheme.colorScheme.primary
MaterialTheme.colorScheme.onPrimary
MaterialTheme.colorScheme.surface
MaterialTheme.colorScheme.error
```

**Extended colors** (import `maia.dmt.core.designsystem.theme.extended`):
```kotlin
MaterialTheme.colorScheme.extended.primaryHover
MaterialTheme.colorScheme.extended.textPrimary
MaterialTheme.colorScheme.extended.textSecondary
MaterialTheme.colorScheme.extended.textPlaceholder
MaterialTheme.colorScheme.extended.textDisabled
MaterialTheme.colorScheme.extended.disabledFill
MaterialTheme.colorScheme.extended.disabledOutline
MaterialTheme.colorScheme.extended.surfaceLower
MaterialTheme.colorScheme.extended.surfaceHigher
MaterialTheme.colorScheme.extended.surfaceOutline
MaterialTheme.colorScheme.extended.success
MaterialTheme.colorScheme.extended.onSuccess
// Accent colors: accentBlue, accentPurple, accentViolet, accentPink, accentOrange, etc.
```

## Responsive Design Pattern

Always use `currentDeviceConfiguration()` for responsive sizing:

```kotlin
val configuration = currentDeviceConfiguration()

// Branch on device type for sizing
val padding = when (configuration) {
    DeviceConfiguration.MOBILE_PORTRAIT -> 8.dp
    DeviceConfiguration.MOBILE_LANDSCAPE -> 10.dp
    DeviceConfiguration.TABLET_PORTRAIT -> 12.dp
    DeviceConfiguration.TABLET_LANDSCAPE -> 14.dp
    DeviceConfiguration.DESKTOP -> 16.dp
}
```

The `DeviceConfiguration` enum values are:
- `MOBILE_PORTRAIT` — narrow phone, portrait
- `MOBILE_LANDSCAPE` — phone in landscape
- `TABLET_PORTRAIT` — tablet portrait
- `TABLET_LANDSCAPE` — tablet landscape
- `DESKTOP` — desktop/large window

## Naming Conventions

- Shared components: `Dmt<Name>.kt` file, `Dmt<Name>` composable function
- Style enums: `Dmt<Name>Style` with UPPER_CASE values
- Feature components: No `Dmt` prefix required (e.g., `DmtModuleSection`, `DmtMessageSection`)
- Previews: `@Composable @Preview fun Dmt<Name>Preview()` at bottom of file
- Wrap previews in `DmtTheme { }` block, add dark variant with `DmtTheme(darkTheme = true)`

## API Design Pattern

Follow this parameter ordering convention:
1. Required data parameters (text, items, values)
2. Required callbacks (onClick, onValueChange)
3. `modifier: Modifier = Modifier`
4. Style/configuration with defaults (style, enabled, isLoading)
5. Optional composable slot lambdas (leadingIcon, trailingContent)
