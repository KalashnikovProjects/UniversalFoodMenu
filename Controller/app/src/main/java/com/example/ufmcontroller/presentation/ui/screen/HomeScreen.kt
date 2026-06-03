package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ufmcontroller.R
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.CategorizedFoodItemsElement
import com.example.ufmcontroller.presentation.ui.component.SearchBar
import com.example.ufmcontroller.presentation.viewmodel.HomeUiState
import com.example.ufmcontroller.presentation.viewmodel.HomeViewModel

// TODO: добавить во viewModel и в какие-то сохранялки между запусками режим отображения (плитки или список)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel= hiltViewModel<HomeViewModel>(),
    navigateEditCategory: (Int) -> Unit,
    navigateEditFoodItem: (Int) -> Unit,
    onToggleDrawer: () -> Unit,
) {
    val uiState: HomeUiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        searchState = viewModel.searchState,
        navigateEditFoodItem = navigateEditFoodItem,
        toggleFoodItem = {id -> viewModel.toggleFoodItem(id) },
        onCategoryVisibilitySwitch = {id -> viewModel.switchCategoryVisibility(id) },
        navigateEditCategory = navigateEditCategory,
        onCategoryToggle = {id -> viewModel.toggleCategory(id) },
        onToggleDrawer = onToggleDrawer,
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    searchState: TextFieldState,
    navigateEditFoodItem: (Int) -> Unit,
    toggleFoodItem: (Int) -> Unit,
    onCategoryVisibilitySwitch: (Int) -> Unit,
    navigateEditCategory: (Int) -> Unit,
    onCategoryToggle: (Int) -> Unit,
    onToggleDrawer: () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(
                top = 12.dp, start = 5.dp, end = 5.dp
            )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onToggleDrawer,
                colors = IconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = colorScheme.onBackground,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = colorScheme.onBackground.copy(alpha = 0.5F),
                ),
                modifier = Modifier.size(60.dp)
            ) {
                Icon(Icons.Filled.Menu,
                    contentDescription = "Settings"
                )
            }
            SearchBar(searchState, Modifier
                .weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.ufm_icon_foreground),
                contentDescription = "UFM logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(60.dp)
            )
        }
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
                toggleFoodItem = { },
                onCategoryVisibilitySwitch = {  },
                navigateEditCategory = {  },
                onCategoryToggle = {  },
                onToggleDrawer = { },
            )
        }
    }
}