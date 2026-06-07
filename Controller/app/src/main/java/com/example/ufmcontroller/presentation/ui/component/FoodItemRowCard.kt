package com.example.ufmcontroller.presentation.ui.component

import android.util.Log
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.ufmcontroller.domain.entity.FoodItem

// showNotInStock - влияет ли есть ли в наличии на отображение
@Composable
fun FoodItemRowCard(item: FoodItem,
                    onFoodItemClick: (Int, Boolean) -> Unit = {_, _ -> },
                    onFoodItemLongClick: (Int) -> Unit = {_ -> },
                    showSwitch: Boolean = false,
                    showNotInStock: Boolean = true,
                    isCheckedUp: Boolean= item.inStock,
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceContainerLow
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp, vertical = 4.dp)
            .combinedClickable(
                onClick = {
                    onFoodItemClick(item.id, !isCheckedUp)
                },
                onLongClick = {
                    onFoodItemLongClick(item.id)
                },
            )
        ,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 10.dp)
        ) {
            if (item.imageUri != null) {
                AsyncImage(
                    model = item.imageUri,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).padding(horizontal = 5.dp),
                    alpha=if (isCheckedUp || !showNotInStock) 1F else 0.3F
                )
            }
            Row(modifier = Modifier
                .weight(1f)
                .padding(start=8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.name,
                    fontSize = 22.sp,
                    textDecoration = if (!isCheckedUp && showNotInStock) TextDecoration.LineThrough else
                        TextDecoration.None,
                    color = colorScheme.onSurface,
                    modifier = Modifier.weight(1f).padding(vertical = 7.dp)
                )
                Text(
                    text = item.price.toString(),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .alpha(0.5F),
                    color = colorScheme.onSurface,
                    softWrap = false,
                    maxLines = 1
                )
            }
            if (showSwitch) {
                Checkbox(checked = isCheckedUp, onCheckedChange = {
                        value ->
                        onFoodItemClick(item.id, !isCheckedUp)
                    },
                    modifier = Modifier.scale(0.8F)
                )
            }
        }
    }
}