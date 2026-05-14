package com.kalashnikovprojects.ufmtv.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem

@Composable
fun FoodItemRowCard(item: FoodItem, onFoodItemToggle: (Int) -> Unit) {
    val elevation = if (item.inStock) 2.dp else 6.dp

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (item.inStock)
                MaterialTheme.colorScheme.surfaceContainerHigh
            else MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        onClick = { onFoodItemToggle(item.id) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 17.dp, horizontal = 10.dp)
        ) {
            if (item.imageUri != null) {
                AsyncImage(
                    model = item.imageUri,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    alpha=if (item.inStock) 1F else 0.3F
                )
            }
            Row(modifier = Modifier
                .weight(1f)
                .padding(start=8.dp),
                verticalAlignment = Alignment.Bottom) {
                Text(
                    text = item.name,
                    fontSize = 22.sp,
                    textDecoration = if (!item.inStock) TextDecoration.LineThrough else
                        TextDecoration.None,
                    color = if (!item.inStock) MaterialTheme.colorScheme.onSurfaceVariant else
                        MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.price.toString(),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(start=6.dp)
                        .alpha(0.5F),
                    color = if (!item.inStock) MaterialTheme.colorScheme.onSurfaceVariant else
                        MaterialTheme.colorScheme.onSurface
                )
            }
//                    IconButton(onClick = {
//                        // TODO: перемещение элемента
//                        },
//                        modifier = Modifier.requiredSize(30.dp)
//                    ) {
//                        Icon(Icons.Filled.Menu,
//                            contentDescription = "move element")
//                    }
        }
    }
}