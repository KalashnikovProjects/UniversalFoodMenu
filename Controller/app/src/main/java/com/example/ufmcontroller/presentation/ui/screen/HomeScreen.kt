package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.CategorizedFoodItemsElement
import com.example.ufmcontroller.presentation.ui.component.DefaultAppTop
import com.example.ufmcontroller.presentation.ui.component.SearchBar
import com.example.ufmcontroller.presentation.viewmodel.HomeUiState
import com.example.ufmcontroller.presentation.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel= hiltViewModel<HomeViewModel>(),
    navigateEditCategory: (Int) -> Unit,
    navigateEditFoodItem: (Int) -> Unit,
    onEditMenu: () -> Unit,
    onToggleDrawer: () -> Unit,
) {
    val uiState: HomeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenContent(
        uiState = uiState,
        searchState = viewModel.searchState,
        navigateEditFoodItem = navigateEditFoodItem,
        toggleFoodItem = viewModel::toggleFoodItem,
        onCategoryVisibilitySwitch = {id -> viewModel.switchCategoryVisibility(id) },
        navigateEditCategory = navigateEditCategory,
        onCategoryToggle = viewModel::toggleCategory,
        onEditMenu=onEditMenu,
        onToggleDrawer = onToggleDrawer,
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    searchState: TextFieldState,
    navigateEditFoodItem: (Int) -> Unit,
    toggleFoodItem: (Int, Boolean) -> Unit,
    onCategoryVisibilitySwitch: (Int) -> Unit,
    navigateEditCategory: (Int) -> Unit,
    onCategoryToggle: (Int, Boolean) -> Unit,
    onEditMenu: () -> Unit,
    onToggleDrawer: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            DefaultAppTop(
                onButton = onToggleDrawer,
                title = {
                    SearchBar(searchState, Modifier
                        .fillMaxWidth())
                }
            )
            if (uiState.items.noCategoryFoodItems.isNotEmpty() ||
                uiState.items.categories.isNotEmpty()) {
                CategorizedFoodItemsElement(
                    uiState.items,
                    opened = uiState.openedIds,
                    onCategoryClick = onCategoryVisibilitySwitch,
                    onCategoryLongClick = navigateEditCategory,
                    onFoodItemClick = toggleFoodItem,
                    onFoodItemLongClick = navigateEditFoodItem,
                    onCategoryToggle = onCategoryToggle,
                    showSwitch = true
                )
            }
        }
        if (uiState.items.noCategoryFoodItems.isEmpty() &&
            uiState.items.categories.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(300.dp)
                ) {
                    Text(
                        if (!uiState.searchTextIsEmpty) "Ничего не найдено"
                            else "Ваше меню пока пустое. Нажмите, чтобы его заполнить.",
                        color=colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        fontSize = 19.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.padding(8.dp)
                    )
                    if (uiState.searchTextIsEmpty) {
                        Button(
                            onClick = onEditMenu,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = "Редактировать меню",
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true, showBackground = true)
@Preview(name = "Dark Mode", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    val searchState by remember { mutableStateOf(TextFieldState("")) }
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize().background(colorScheme.background)
        ) {
            HomeScreenContent(
                uiState = HomeUiState(
                    items = FoodItemsCategorized(
                        listOf(
                            CategoryWithFoodItems(
                                Category(
                                    id = 14,
                                    name = "Фастфуд",
                                    imageUri = null,
                                    price = null,
                                    inStock = null,
                                ),
                                foodItems = listOf(
                                    FoodItem(
                                        1,
                                        "Американский бургер",
                                        imageUri = "https://static.vecteezy.com/system/resources/previews/041/290/624/non_2x/ai-generated-fresh-burger-isolated-on-transparent-background-free-png.png",
                                        price = 99.99F,
                                        inStock = true
                                    ),
                                    FoodItem(
                                        2,
                                        "Итальянская пицца",
                                        imageUri = null,
                                        price = 99.99F,
                                        inStock = false
                                    )
                                )
                            ),
                            CategoryWithFoodItems(
                                Category(
                                    id = 15,
                                    name = "Фастфуд 2",
                                    imageUri = "https://static.vecteezy.com/system/resources/previews/041/290/624/non_2x/ai-generated-fresh-burger-isolated-on-transparent-background-free-png.png",
                                    price = 100.99F,
                                    inStock = false,
                                ),
                                foodItems = listOf(
                                    FoodItem(
                                        1,
                                        "Американский бургер",
                                        imageUri = "https://static.vecteezy.com/system/resources/previews/041/290/624/non_2x/ai-generated-fresh-burger-isolated-on-transparent-background-free-png.png",
                                        price = 99.99F,
                                        inStock = true
                                    ),
                                    FoodItem(
                                        2,
                                        "Итальянская пицца",
                                        imageUri = null,
                                        price = 99.99F,
                                        inStock = false
                                    )
                                )
                            )
                        ),
                        listOf(
                            FoodItem(
                                2,
                                "Китайский вок",
                                imageUri = null,
                                price = 99.99F,
                                inStock = false
                            ),
                            FoodItem(
                                1,
                                "Японские роллы",
                                imageUri = null,
                                price = 99.99F,
                                inStock = true
                            )
                        )
                    ),
                    openedIds = setOf(14, -1),
                ),
                searchState = searchState,
                navigateEditFoodItem = {},
                toggleFoodItem = { _, _ -> },
                onCategoryVisibilitySwitch = {  },
                navigateEditCategory = {  },
                onCategoryToggle = { _, _ -> },
                onEditMenu = {},
                onToggleDrawer = { },
            )
        }
    }
}


@Preview(name = "Light Mode", showSystemUi = true, showBackground = true)
@Preview(name = "Dark Mode", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenEmptyPreview() {
    val searchState by remember { mutableStateOf(TextFieldState("")) }
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize().background(colorScheme.background)
        ) {
            HomeScreenContent(
                uiState = HomeUiState(
                    items = FoodItemsCategorized(
                        emptyList(),
                        emptyList(),
                    ),
                    openedIds = setOf(14, -1),
                ),
                searchState = searchState,
                navigateEditFoodItem = {},
                toggleFoodItem = { _, _ -> },
                onCategoryVisibilitySwitch = {  },
                navigateEditCategory = {  },
                onCategoryToggle = { _, _ -> },
                onEditMenu = {},
                onToggleDrawer = { },
            )
        }
    }
}