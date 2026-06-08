package com.example.ufmcontroller.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.presentation.viewmodel.AddDesignExtended
import com.example.ufmcontroller.presentation.viewmodel.InputStates
import com.example.ufmcontroller.presentation.viewmodel.TvScreenUiState

@Composable
fun AddItemTabContent(
    uiState: TvScreenUiState,
    inputStates: InputStates,
    onAddFoodItem: (Int) -> Unit,
    onAddCategory: (Int) -> Unit,
    onAddText: () -> Unit,
    onAddImage: () -> Unit,
    onHeaderClick: (AddDesignExtended) -> Unit,
    onGalleryClick: () -> Unit,
    navigateEditFoodItem: (Int) -> Unit,
    navigateEditCategory: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
    ) {
        item {
            ExpandableSection(
                title = "Позиции меню",
                isExpanded = uiState.addDesignExtended == AddDesignExtended.FOOD_ITEMS,
                onHeaderClick = {
                    onHeaderClick(
                        if (uiState.addDesignExtended == AddDesignExtended.FOOD_ITEMS) AddDesignExtended.NONE
                        else AddDesignExtended.FOOD_ITEMS
                    )
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SearchBar(inputStates.foodItemsSearchState, modifier = Modifier.padding(5.dp).fillMaxWidth())
                    uiState.foodItems.forEach { item ->
                        key("foodItem_for_design_item_selection${item.id}") {
                            FoodItemRowCard(
                                item = item,
                                onFoodItemClick = { id, _ -> onAddFoodItem(id) },
                                onFoodItemLongClick = navigateEditFoodItem,
                                showSwitch = false,
                                showNotInStock = false,
                            )
                        }
                    }
                }
            }
        }

        item {
            ExpandableSection(
                title = "Категории",
                isExpanded = uiState.addDesignExtended == AddDesignExtended.CATEGORIES,
                onHeaderClick = {
                    onHeaderClick(
                        if (uiState.addDesignExtended == AddDesignExtended.CATEGORIES) AddDesignExtended.NONE
                        else AddDesignExtended.CATEGORIES
                    )
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SearchBar(inputStates.categoriesSearchState, modifier = Modifier.padding(5.dp).fillMaxWidth())
                    uiState.categories.forEach { item ->
                        key("category_for_design_item_selection${item.id}") {
                            CategoryElement(
                                category = CategoryWithFoodItems(category = item, emptyList()),
                                opened = false,
                                onFoodItemClick = { _, _ -> },
                                onFoodItemLongClick = { },
                                showSwitch = false,
                                onCategoryClick = { id -> onAddCategory(id) },
                                onCategoryLongClick = navigateEditCategory,
                                onCategoryToggle = { _, _ -> },
                                showExpand = false,
                                showNotInStock = false,
                                showBG = true,
                                doSpaceIfNoExpand = false,
                            )
                        }
                    }
                }
            }
        }

        item {
            ExpandableSection(
                title = "Текст",
                isExpanded = uiState.addDesignExtended == AddDesignExtended.TEXT,
                onHeaderClick = {
                    onHeaderClick(
                        if (uiState.addDesignExtended == AddDesignExtended.TEXT) AddDesignExtended.NONE
                        else AddDesignExtended.TEXT
                    )
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        state = inputStates.textItemFieldState,
                        placeholder = { Text("Текст", modifier = Modifier.alpha(0.5F)) },
                        modifier = Modifier.clip(RoundedCornerShape(7.dp)).padding(vertical = 5.dp),
                    )
                    Button(onClick = onAddText, shape = MaterialTheme.shapes.medium) {
                        Text("Добавить")
                    }
                }
            }
        }

        item {
            ExpandableSection(
                title = "Изображение",
                isExpanded = uiState.addDesignExtended == AddDesignExtended.IMAGE,
                onHeaderClick = {
                    onHeaderClick(
                        if (uiState.addDesignExtended == AddDesignExtended.IMAGE) AddDesignExtended.NONE
                        else AddDesignExtended.IMAGE
                    )
                }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = onGalleryClick) {
                        Text("Выбрать из галереи")
                    }
                    if (uiState.uploadedImagUri != null) {
                        AsyncImage(
                            model = uiState.uploadedImagUri,
                            contentDescription = null,
                            modifier = Modifier.padding(bottom = 10.dp).size(100.dp).clip(RoundedCornerShape(7.dp)),
                        )
                        Button(onClick = onAddImage, shape = MaterialTheme.shapes.medium) {
                            Text("Добавить")
                        }
                    }
                }
            }
        }
    }
}