package maia.dmt.market.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmtproms.cogtest.market.presentation.generated.resources.Res
import dmtproms.cogtest.market.presentation.generated.resources.conveyor_new
import maia.dmt.core.designsystem.theme.extended
import maia.dmt.market.domain.model.SelectionState
import maia.dmt.market.presentation.mapper.RecipePresentationMapper
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.StringResource

@Composable
fun GroceryChecklist(
    groceries: List<StringResource>,
    checked: List<Boolean>,
    modifier: Modifier = Modifier
) {
    val col1 = groceries.take(3)
    val col2 = groceries.drop(3).take(3)
    val col3 = groceries.drop(6)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            col1.forEachIndexed { i, res ->
                GroceryCheckRow(
                    text = stringResource(res),
                    checked = checked.getOrNull(i) ?: false
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            col2.forEachIndexed { i, res ->
                GroceryCheckRow(
                    text = stringResource(res),
                    checked = checked.getOrNull(i + 3) ?: false
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            col3.forEachIndexed { i, res ->
                GroceryCheckRow(
                    text = stringResource(res),
                    checked = checked.getOrNull(i + 6) ?: false
                )
            }
        }
    }
}

@Composable
private fun GroceryCheckRow(
    text: String,
    checked: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            enabled = false,
            modifier = Modifier.size(28.dp),
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Right,
            color = MaterialTheme.colorScheme.extended.textPrimary,
        )
    }
}

@Composable
fun GroceryChip(
    groceryId: String,
    selection: SelectionState,
    modifier: Modifier = Modifier
) {
    val mapper = RecipePresentationMapper()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(220.dp)
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(Color.White, CircleShape)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            val imageRes = mapper.getGroceryImageResource(groceryId)
            val displayName = stringResource(mapper.getGroceryStringResource(groceryId))

            if (imageRes != null) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = displayName,
                    modifier = Modifier.fillMaxSize(0.75f),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    text = displayName.take(4),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.extended.textSecondary,
                    textAlign = TextAlign.Center
                )
            }

            when (selection) {
                SelectionState.CORRECT -> SelectionBadge(
                    symbol = "✓",
                    color = Color(0xFF4CAF50)
                )
                SelectionState.WRONG -> SelectionBadge(
                    symbol = "✕",
                    color = Color(0xFFE53935)
                )
                SelectionState.UNSELECTED -> {}
            }
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .width(180.dp)
                .height(50.dp)
                .background(Color(0xFF5A5A5A), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(mapper.getGroceryStringResource(groceryId)),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                maxLines = 2,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun BoxScope.SelectionBadge(symbol: String, color: Color) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .align(Alignment.TopEnd)
            .offset(x = 10.dp, y = (-10).dp)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            fontSize = 35.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ConveyorBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(Res.drawable.conveyor_new),
            contentDescription = "Conveyor belt",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun RecipeImage(
    recipeId: String,
    modifier: Modifier = Modifier
) {
    val mapper = RecipePresentationMapper()
    val imageRes = mapper.getRecipeImageResource(recipeId)
    val titleRes = mapper.getRecipeTitleResource(recipeId)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = stringResource(titleRes),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}