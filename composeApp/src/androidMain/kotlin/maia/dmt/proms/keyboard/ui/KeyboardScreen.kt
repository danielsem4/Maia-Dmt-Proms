package maia.dmt.proms.keyboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class KeyboardMode {
    QWERTY, SYMBOLS, HEBREW, EMOJI
}

@Composable
fun KeyboardScreen(
    onKeyPress: (Int) -> Unit,
    onDelete: () -> Unit,
    onSpace: () -> Unit,
    onDone: () -> Unit,
    onModeChange: (KeyboardMode) -> Unit,
    currentMode: KeyboardMode = KeyboardMode.QWERTY
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF000000))
            .padding(4.dp)
    ) {
        when (currentMode) {
            KeyboardMode.QWERTY -> QwertyLayout(onKeyPress, onDelete, onSpace, onDone, onModeChange)
            KeyboardMode.SYMBOLS -> SymbolsLayout(onKeyPress, onDelete, onSpace, onDone, onModeChange)
            KeyboardMode.HEBREW -> HebrewLayout(onKeyPress, onDelete, onSpace, onDone, onModeChange)
            KeyboardMode.EMOJI -> EmojiLayout(onKeyPress, onDelete, onSpace, onDone, onModeChange)
        }
    }
}

@Composable
private fun QwertyLayout(
    onKeyPress: (Int) -> Unit,
    onDelete: () -> Unit,
    onSpace: () -> Unit,
    onDone: () -> Unit,
    onModeChange: (KeyboardMode) -> Unit
) {
    Column {
        KeyRow {
            listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p").forEach { key ->
                KeyButton(text = key) { onKeyPress(key[0].code) }
            }
        }

        KeyRow {
            listOf("a", "s", "d", "f", "g", "h", "j", "k", "l").forEach { key ->
                KeyButton(text = key) { onKeyPress(key[0].code) }
            }
        }

        KeyRow {
            KeyButton(text = "⇧", modifier = Modifier.weight(1.5f)) { /* Shift */ }
            listOf("z", "x", "c", "v", "b", "n", "m").forEach { key ->
                KeyButton(text = key) { onKeyPress(key[0].code) }
            }
            KeyButton(text = "⌫", modifier = Modifier.weight(1.5f)) { onDelete() }
        }

        // Row 4
        KeyRow {
            KeyButton(text = "123", modifier = Modifier.weight(1.2f)) {
                onModeChange(KeyboardMode.SYMBOLS)
            }
            KeyButton(text = "😀", modifier = Modifier.weight(1.2f)) {
                onModeChange(KeyboardMode.EMOJI)
            }
            KeyButton(text = "עב", modifier = Modifier.weight(1.2f)) {
                onModeChange(KeyboardMode.HEBREW)
            }
            KeyButton(text = "Space", modifier = Modifier.weight(4f)) { onSpace() }
            KeyButton(text = "Done", modifier = Modifier.weight(1.5f)) { onDone() }
        }
    }
}

@Composable
private fun SymbolsLayout(
    onKeyPress: (Int) -> Unit,
    onDelete: () -> Unit,
    onSpace: () -> Unit,
    onDone: () -> Unit,
    onModeChange: (KeyboardMode) -> Unit
) {
    Column {
        KeyRow {
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").forEach { key ->
                KeyButton(text = key) { onKeyPress(key[0].code) }
            }
        }

        KeyRow {
            listOf("@", "#", "$", "%", "&", "*", "-", "+", "(", ")").forEach { key ->
                KeyButton(text = key) { onKeyPress(key[0].code) }
            }
        }

        KeyRow {
            listOf("!", "\"", "'", ":", ";", "/", "?", ".", ",").forEach { key ->
                KeyButton(text = key) { onKeyPress(key[0].code) }
            }
            KeyButton(text = "⌫", modifier = Modifier.weight(1.5f)) { onDelete() }
        }

        KeyRow {
            KeyButton(text = "ABC", modifier = Modifier.weight(2f)) {
                onModeChange(KeyboardMode.QWERTY)
            }
            KeyButton(text = "Space", modifier = Modifier.weight(5f)) { onSpace() }
            KeyButton(text = "Done", modifier = Modifier.weight(2f)) { onDone() }
        }
    }
}

@Composable
private fun HebrewLayout(
    onKeyPress: (Int) -> Unit,
    onDelete: () -> Unit,
    onSpace: () -> Unit,
    onDone: () -> Unit,
    onModeChange: (KeyboardMode) -> Unit
) {
    Column {
        KeyRow {
            listOf("ק", "ר", "א", "ט", "ו", "ן", "ם", "פ", "ש", "ד").forEach { key ->
                KeyButton(text = key) { onKeyPress(key[0].code) }
            }
        }

        KeyRow {
            listOf("י", "ח", "ל", "כ", "ע", "ת", "צ", "מ", "נ").forEach { key ->
                KeyButton(text = key) { onKeyPress(key[0].code) }
            }
        }

        KeyRow {
            KeyButton(text = "⇧", modifier = Modifier.weight(1.5f)) { /* Shift */ }
            listOf("ז", "ס", "ב", "ה", "נ", "ג").forEach { key ->
                KeyButton(text = key) { onKeyPress(key[0].code) }
            }
            KeyButton(text = "⌫", modifier = Modifier.weight(1.5f)) { onDelete() }
        }

        KeyRow {
            KeyButton(text = "123", modifier = Modifier.weight(1.2f)) {
                onModeChange(KeyboardMode.SYMBOLS)
            }
            KeyButton(text = "😀", modifier = Modifier.weight(1.2f)) {
                onModeChange(KeyboardMode.EMOJI)
            }
            KeyButton(text = "EN", modifier = Modifier.weight(1.2f)) {
                onModeChange(KeyboardMode.QWERTY)
            }
            KeyButton(text = "רווח", modifier = Modifier.weight(4f)) { onSpace() }
            KeyButton(text = "סיום", modifier = Modifier.weight(1.5f)) { onDone() }
        }
    }
}

@Composable
private fun EmojiLayout(
    onKeyPress: (Int) -> Unit,
    onDelete: () -> Unit,
    onSpace: () -> Unit,
    onDone: () -> Unit,
    onModeChange: (KeyboardMode) -> Unit
) {
    Column {
        KeyRow {
            listOf("😀", "😁", "😂", "😃", "😄", "😅", "😆", "😇", "😈", "😉").forEach { emoji ->
                KeyButton(text = emoji) { onKeyPress(emoji.codePointAt(0)) }
            }
        }

        KeyRow {
            listOf("😊", "😋", "😌", "😍", "😎", "😏", "😐", "😑", "😒", "😓").forEach { emoji ->
                KeyButton(text = emoji) { onKeyPress(emoji.codePointAt(0)) }
            }
        }

        KeyRow {
            listOf("😔", "😕", "😖", "😗", "😘", "😙", "😚", "😛", "😜").forEach { emoji ->
                KeyButton(text = emoji) { onKeyPress(emoji.codePointAt(0)) }
            }
            KeyButton(text = "⌫", modifier = Modifier.weight(1.5f)) { onDelete() }
        }

        // Row 4
        KeyRow {
            KeyButton(text = "ABC", modifier = Modifier.weight(2f)) {
                onModeChange(KeyboardMode.QWERTY)
            }
            KeyButton(text = "Space", modifier = Modifier.weight(5f)) { onSpace() }
            KeyButton(text = "Done", modifier = Modifier.weight(2f)) { onDone() }
        }
    }
}

@Composable
private fun KeyRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        content()
    }
}

@Composable
private fun RowScope.KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF2C2C2E))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}