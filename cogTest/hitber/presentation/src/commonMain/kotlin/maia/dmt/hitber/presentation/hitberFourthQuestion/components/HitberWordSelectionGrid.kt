package maia.dmt.hitber.presentation.hitberFourthQuestion.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dmtproms.cogtest.hitber.presentation.generated.resources.Res
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_ball
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_balloon
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_book
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_glasses
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_key
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_lemon
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_pencil
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_phone
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_ruler
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_shoe
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_table
import dmtproms.cogtest.hitber.presentation.generated.resources.cogTest_hitber_word_watch
import maia.dmt.core.designsystem.components.cards.DmtCardStyle
import maia.dmt.core.designsystem.components.cards.DmtParagraphCard
import maia.dmt.hitber.presentation.hitberFourthQuestion.HitberWord
import org.jetbrains.compose.resources.stringResource

@Composable
fun HitberWordSelectionGrid(
    options: List<HitberWord>,
    selectedWord: HitberWord?,
    columns: Int,
    onWordClick: (HitberWord) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rows = options.chunked(columns)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                rowItems.forEach { word ->
                    val isSelected = word == selectedWord
                    DmtParagraphCard(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onWordClick(word) },
                        text = word.label(),
                        style = if (isSelected) DmtCardStyle.PRIMARY else DmtCardStyle.ELEVATED,
                        textSize = MaterialTheme.typography.bodySmall,
                    )
                }
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun HitberWord.label(): String = when (this) {
    HitberWord.PENCIL -> stringResource(Res.string.cogTest_hitber_word_pencil)
    HitberWord.RULER -> stringResource(Res.string.cogTest_hitber_word_ruler)
    HitberWord.TABLE -> stringResource(Res.string.cogTest_hitber_word_table)
    HitberWord.BALL -> stringResource(Res.string.cogTest_hitber_word_ball)
    HitberWord.BALLOON -> stringResource(Res.string.cogTest_hitber_word_balloon)
    HitberWord.LEMON -> stringResource(Res.string.cogTest_hitber_word_lemon)
    HitberWord.KEY -> stringResource(Res.string.cogTest_hitber_word_key)
    HitberWord.WATCH -> stringResource(Res.string.cogTest_hitber_word_watch)
    HitberWord.GLASSES -> stringResource(Res.string.cogTest_hitber_word_glasses)
    HitberWord.SHOE -> stringResource(Res.string.cogTest_hitber_word_shoe)
    HitberWord.PHONE -> stringResource(Res.string.cogTest_hitber_word_phone)
    HitberWord.BOOK -> stringResource(Res.string.cogTest_hitber_word_book)
}
