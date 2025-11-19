package maia.dmt.market.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import maia.dmt.core.designsystem.components.buttons.DmtButton
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun RecipeCategoryItemPortrait(
    recipeId: String,
    title: String,
    imageRes: DrawableResource,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    compactMode: Boolean = false
) {
    val cardPadding = if (compactMode) 8.dp else 16.dp
    val innerPadding = if (compactMode) 8.dp else 16.dp
    val spacing = if (compactMode) 8.dp else 16.dp
    val buttonHeight = if (compactMode) 48.dp else 56.dp

    Card(
        modifier = modifier
            .padding(horizontal = cardPadding, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = title,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .weight(1f),
                contentScale = ContentScale.Crop
            )

            DmtButton(
                text = title,
                onClick = { onCategoryClick(recipeId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = buttonHeight)
            )
        }
    }
}

@Composable
fun RecipeCategoryItemLandscape(
    recipeId: String,
    title: String,
    imageRes: DrawableResource,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(2.dp)
            .wrapContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(1f))

            DmtButton(
                text = title,
                onClick = { onCategoryClick(recipeId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            )
        }
    }
}