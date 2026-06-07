package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.CategoryElement
import com.example.ufmcontroller.presentation.ui.component.DefaultAppTop
import com.example.ufmcontroller.presentation.ui.component.FoodItemRowCard
import com.example.ufmcontroller.presentation.ui.component.SearchBar
import com.example.ufmcontroller.presentation.viewmodel.EditMenuTab
import com.example.ufmcontroller.presentation.viewmodel.EditMenuUiState
import com.example.ufmcontroller.presentation.viewmodel.EditMenuViewModel
import com.example.ufmcontroller.presentation.viewmodel.LoginStep
import com.example.ufmcontroller.presentation.viewmodel.LoginTab
import com.example.ufmcontroller.presentation.viewmodel.LoginUiState

@Composable
fun EditMenuScreen(
    viewModel: EditMenuViewModel = hiltViewModel<EditMenuViewModel>(),
    onNavigateToEditFoodItem: (Int) -> Unit,
    onNavigateToEditCategory: (Int) -> Unit,
    onNavigateToAddFoodItem: () -> Unit,
    onNavigateToAddCategoryItem: () -> Unit,
    onToggleDrawer: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EditMenuScreenContent(
        uiState,
        viewModel.foodSearchState,
        viewModel.categoriesSearchState,
        onNavigateToEditFoodItem,
        onNavigateToEditCategory,
        onNavigateToAddFoodItem,
        onNavigateToAddCategoryItem,
        {
            tab ->
            viewModel.selectTab(tab)
        },
        onToggleDrawer,
    )
}

@Composable
fun EditMenuScreenContent(
    uiState: EditMenuUiState,
    foodSearchState: TextFieldState,
    categorySearchState: TextFieldState,
    onNavigateToEditFoodItem: (Int) -> Unit,
    onNavigateToEditCategory: (Int) -> Unit,
    onNavigateToAddFoodItem: () -> Unit,
    onNavigateToAddCategoryItem: () -> Unit,
    selectTab: (EditMenuTab) -> Unit,
    onToggleDrawer: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            DefaultAppTop("Редактирование меню", onToggleDrawer, true)
            SecondaryTabRow (
                selectedTabIndex = uiState.editMenuTab.ordinal,
                containerColor = colorScheme.surfaceContainerHigh,
                contentColor = colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(100))
            ) {
                val tabs = EditMenuTab.entries

                tabs.forEach { tab ->
                    Tab(
                        selected = uiState.editMenuTab == tab,
                        onClick = { selectTab(tab) },
                        text = {
                            val tabText = when (tab) {
                                EditMenuTab.FOOD -> "Позиции меню"
                                EditMenuTab.CATEGORIES -> "Категории"
                            }
                            Text(
                                text = tabText,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                color=colorScheme.onSurfaceVariant,
                                fontSize = 18.sp,
                            )
                        }
                    )
                }
            }
            Crossfade(
                targetState = uiState.editMenuTab,
                label = "AuthScreenTransition"
            ) {
                    tab ->
                val searchState = if (tab == EditMenuTab.FOOD) foodSearchState else categorySearchState
                Column {
                    SearchBar(searchState, Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp, vertical = 3.dp))
                    HorizontalDivider(Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 25.dp))
                    if (tab == EditMenuTab.FOOD) {
                        if (uiState.foodItems.isNotEmpty()) {
                            LazyColumn(
                                modifier=Modifier.padding(horizontal = 20.dp)
                            ) {
                                items(uiState.foodItems) {
                                        item ->
                                    key("food_element_edit_list${item.id}") {
                                        FoodItemRowCard(
                                            item = item,
                                            onFoodItemClick = { id, _ -> onNavigateToEditFoodItem(id) },
                                            onFoodItemLongClick = onNavigateToEditFoodItem,
                                            showSwitch = false,
                                            showNotInStock = false,
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                "Нету элементов меню",
                                color=colorScheme.onBackground,
                                fontSize = 17.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        if (uiState.categories.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.padding(horizontal = 20.dp)
                            ) {
                                items(uiState.categories) { item ->
                                    key("food_element_edit_list${item.id}") {
                                        CategoryElement(
                                            category = CategoryWithFoodItems(
                                                category = item,
                                                emptyList(),
                                            ),
                                            opened = false,
                                            onFoodItemClick = { _, _ -> },
                                            onFoodItemLongClick = { },
                                            showSwitch = false,
                                            onCategoryClick = onNavigateToEditCategory,
                                            onCategoryLongClick = onNavigateToEditCategory,
                                            onCategoryToggle = { _, _ -> },
                                            showExpand = false,
                                            showNotInStock = false,
                                            showBG = true,
                                            doSpaceIfNoExpand = false,
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                "Нету категорий",
                                color=colorScheme.onBackground,
                                fontSize = 17.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        Crossfade(
            targetState = uiState.editMenuTab,
            label = "EditMenuTabTransition",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
        ) {
            tab ->
            FloatingActionButton(
                onClick = if (tab == EditMenuTab.FOOD) onNavigateToAddFoodItem else onNavigateToAddCategoryItem,
            ) {
                Icon(Icons.Filled.Add,
                    "Add",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(40.dp),
                )
            }
        }
    }
}

@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditMenuScreenContentPreview() {
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            var uiState by remember {
                mutableStateOf(
                    EditMenuUiState(
                        editMenuTab = EditMenuTab.CATEGORIES,
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
                            ),
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
                        ),
                        categories = listOf(
                            Category(
                                id = 14,
                                name = "Фастфуд",
                                imageUri = null,
                                price = null,
                                inStock = null,
                            ),
                            Category(
                                id = 15,
                                name = "Фастфуд 2",
                                imageUri = "https://static.vecteezy.com/system/resources/previews/041/290/624/non_2x/ai-generated-fresh-burger-isolated-on-transparent-background-free-png.png",
                                price = 100.99F,
                                inStock = false,
                            ),
                        )
                    )
                )
            }
            EditMenuScreenContent(
                uiState,
                rememberTextFieldState("Поиск 1"),
                rememberTextFieldState("Поиск 2"),
                {},
                { },
                { },
                { },
                selectTab = { tab ->
                    uiState = uiState.copy(editMenuTab = tab)
                },
                { },
            )
        }
    }
}

@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditMenuScreenContentEmptyPreview() {
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            var uiState by remember {
                mutableStateOf(
                    EditMenuUiState(
                        editMenuTab = EditMenuTab.CATEGORIES,
                        foodItems = emptyList(),
                        categories = emptyList()
                    )
                )
            }
            EditMenuScreenContent(
                uiState,
                rememberTextFieldState("Поиск 1"),
                rememberTextFieldState("Поиск 2"),
                {},
                { },
                { },
                { },
                selectTab = { tab ->
                    uiState = uiState.copy(editMenuTab = tab)
                },
                { },
            )
        }
    }
}
