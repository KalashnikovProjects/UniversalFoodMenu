package com.example.ufmcontroller.presentation.ui.component

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized

@Composable
fun CategorizedFoodItemsElement(categorized: FoodItemsCategorized,
                                opened: Set<Int>,
                                onCategoryClick: (Int) -> Unit,
                                onCategoryLongClick: (Int) -> Unit,
                                onFoodItemClick: (Int) -> Unit,
                                onFoodItemLongClick: (Int) -> Unit,
                                onCategoryToggle: (Int) -> Unit={ _ -> },
                                showSwitch: Boolean=false,
                                showNotInStock: Boolean = true,
                                ) {
    LazyColumn {
        items(categorized.categories) {
                item ->
            key("category_element${item.category.id}") {
                CategoryElement(
                    category = item,
                    opened = opened.contains(item.category.id),
                    onCategoryClick = onCategoryClick,
                    onCategoryLongClick = onCategoryLongClick,
                    onFoodItemClick = onFoodItemClick,
                    onFoodItemLongClick = onFoodItemLongClick,
                    onCategoryToggle = onCategoryToggle,
                    showSwitch = showSwitch,
                    showNotInStock=showNotInStock
                )
            }
        }
    }
    if (categorized.noCategoryFoodItems.isNotEmpty()) {
        Spacer(modifier = Modifier.size(5.dp))
    }
    CategoryElement(
        category = CategoryWithFoodItems(
            Category(
                id = -1,
                name = "Вне категорий",
                imageUri = null,
                price = null,
                inStock = null,
            ),
            categorized.noCategoryFoodItems
        ),
        opened = true,
        onCategoryClick = { },
        onCategoryLongClick = { },
        onFoodItemClick = onFoodItemClick,
        onFoodItemLongClick = onCategoryLongClick,
        onCategoryToggle = {  },
        showSwitch = false,
        showChildSwitch=showSwitch,
        showExpand = false,
        showHead = categorized.categories.isNotEmpty(),
        showNotInStock=showNotInStock
    )
}