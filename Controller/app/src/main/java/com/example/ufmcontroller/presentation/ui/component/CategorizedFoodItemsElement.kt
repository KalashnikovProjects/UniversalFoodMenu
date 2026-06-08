package com.example.ufmcontroller.presentation.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized

@Composable
fun CategorizedFoodItemsElement(categorized: FoodItemsCategorized,
                                opened: Set<Int> = emptySet(),
                                onCategoryClick: (Int) -> Unit = {},
                                onCategoryLongClick: (Int) -> Unit = {},
                                onFoodItemClick: (Int, Boolean) -> Unit = {_, _ -> },
                                onFoodItemLongClick: (Int) -> Unit = {},
                                onCategoryToggle: (Int, Boolean) -> Unit = {_, _ -> },
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
                    showNotInStock=showNotInStock,
                    showBG = true
                )
            }
        }
        if (categorized.noCategoryFoodItems.isNotEmpty()) {
            item(key = "no_category_element") {
                Spacer(modifier = Modifier.size(5.dp))
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
                    onFoodItemLongClick = onFoodItemLongClick,
                    onCategoryToggle = { _, _ ->  },
                    showSwitch = false,
                    showChildSwitch=showSwitch,
                    showExpand = false,
                    showHead = categorized.categories.isNotEmpty(),
                    showNotInStock=showNotInStock
                )
            }

        }
    }
}