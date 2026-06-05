package com.example.ufmcontroller.presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems

@Composable
fun CategoryElement(category: CategoryWithFoodItems,
                    isCheckedUp: Boolean=category.category.inStock != false,
                    opened: Boolean=false,
                    onCategoryClick: (Int) -> Unit = {},
                    onCategoryLongClick: (Int) -> Unit = {},
                    onFoodItemClick: (Int) -> Unit = {},
                    onFoodItemLongClick: (Int) -> Unit = {},
                    onCategoryToggle: (Int) -> Unit = {},
                    showSwitch: Boolean=false,
                    showChildSwitch: Boolean=showSwitch,
                    showExpand: Boolean=true,
                    showHead:  Boolean=true,
                    showNotInStock: Boolean = true,
                    showBG: Boolean=false,
                    doSpaceIfNoExpand: Boolean=true,
                    categoryFontSize: TextUnit = 26.sp,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (!showBG) colorScheme.surfaceColorAtElevation((-0.3).dp) else colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        if (showHead) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .combinedClickable(
                        onClick = { onCategoryClick(category.category.id) },
                        onLongClick = { onCategoryLongClick(category.category.id) }
                    )
                    .padding(vertical = 7.dp, horizontal = 10.dp)
                    .fillMaxWidth()
            ) {
                if (showExpand) {
                    IconButton(
                        onClick = { onCategoryClick(category.category.id) },
                    ) {
                        Icon(if (!opened) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
                            contentDescription = "Expand category",
                            tint = colorScheme.onBackground,
                        )
                    }
                } else {
                    if (doSpaceIfNoExpand) {
                        Spacer(modifier = Modifier.size(15.dp))
                    } else {
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
                if (category.category.imageUri != null) {
                    AsyncImage(
                        model = category.category.imageUri,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 10.dp).size(40.dp),
                        alpha=if (category.category.inStock != false || !showNotInStock) 1F else 0.3F
                    )
                }
                Text(category.category.name,
                    fontSize = categoryFontSize,
                    modifier = Modifier
                        .weight(1F),
                    textDecoration = if (category.category.inStock == false && showNotInStock) TextDecoration.LineThrough else
                        TextDecoration.None,
                    color = colorScheme.onBackground,
                )
                if (category.category.price != null) {
                    Text(
                        text = category.category.price.toString(),
                        fontSize = categoryFontSize / 2,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .alpha(0.6F),
                        color = colorScheme.onSurface,
                        softWrap = false,
                        maxLines = 1
                    )
                }


                if (showSwitch) {
                    Checkbox(checked = isCheckedUp, onCheckedChange = {
                        onCategoryToggle(category.category.id)
                    },
                        modifier = Modifier.scale(0.8F)
                    )
                }
            }
        }
        AnimatedVisibility(opened) {
            Column(
                modifier = Modifier.padding(start=10.dp)
            ) {
                for (item in category.foodItems) {
                    FoodItemRowCard(item = item,
                        { onFoodItemClick(item.id) },
                        { onFoodItemLongClick(item.id) },
                        showSwitch=showChildSwitch,
                        showNotInStock=showNotInStock
                    )
                }
            }
        }
    }
}