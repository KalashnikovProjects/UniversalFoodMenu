package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemDisplayTypeStyle
import com.example.ufmcontroller.domain.entity.ImageItem
import com.example.ufmcontroller.domain.entity.NotInStockStyle
import com.example.ufmcontroller.domain.entity.ScreenStyle
import com.example.ufmcontroller.domain.entity.ScreenTheme
import com.example.ufmcontroller.domain.entity.Style
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.entity.TextItem
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.DefaultAppTop
import com.example.ufmcontroller.presentation.ui.component.TvScreenCard
import com.example.ufmcontroller.presentation.viewmodel.VisualConfigurationScreenUiState
import com.example.ufmcontroller.presentation.viewmodel.VisualConfigurationViewModel


@Composable
fun VisualConfigurationScreen(
    viewModel: VisualConfigurationViewModel = hiltViewModel<VisualConfigurationViewModel>(),
    onNavigateToScreen: (Int) -> Unit,
    onAddScreen: () -> Unit,
    onToggleDrawer: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VisualConfigurationScreenContent(
        uiState,
        onNavigateToScreen,
        onAddScreen,
        onToggleDrawer,
    )
}

@Composable
fun VisualConfigurationScreenContent(
    uiState: VisualConfigurationScreenUiState,
    onNavigateToScreen: (Int) -> Unit,
    onAddScreen: () -> Unit,
    onToggleDrawer: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            DefaultAppTop("Настройка экранов", onButton = onToggleDrawer)
            LazyVerticalGrid (
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(uiState.screens) {
                        item ->
                    key(
                        "tv_screen_card${item.tvScreen.id}"
                    ) {
                        TvScreenCard(
                            screenWithDesignItems=item,
                            onNavigateToScreen=onNavigateToScreen,
                            clickable = true,
                            fontSize = 8.sp,
                        )
                    }
                }
            }
        }
        if (uiState.screens.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(300.dp)
                ) {
                    Text(
                        "Не привязано ни одного экрана. Нажмите, чтобы добавить первый.",
                        color=colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        fontSize = 19.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier.padding(8.dp)
                    )
                    Button(
                        onClick = onAddScreen,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "Добавить экран",
                        )
                    }
                }
            }
        }
        ExtendedFloatingActionButton(
            onClick = onAddScreen,
            icon = { Icon(Icons.Filled.Add, "Add") },
            text = { Text(text = "Добавить экран") },
            modifier = Modifier.align(Alignment.BottomEnd).padding(30.dp)
        )
    }
}

@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VisualConfigurationScreenPreview() {
    val uiState = VisualConfigurationScreenUiState(
        screens = listOf(
            TVScreenWithDesignItems(
                TVScreen(
                    0,
                    "Первый экран",
                    800,
                    600,
                    ScreenStyle(),
                ),
                designItems = emptyList()
            ),
            TVScreenWithDesignItems(
                TVScreen(
                    1,
                    "Экран Redmi Super Max Ultra Pro 42",
                    1920,
                    1080,
                    ScreenStyle(
                        screenTheme = ScreenTheme.BLACK,
                    ),
                ),
                designItems = listOf(
                    DesignItem(
                        1,
                        TextItem(
                            1,
                            "Бургерная",
                        ),
                        Style(
                            x = 0.1F,
                            y = 0.1F,
                            scale = 3F,
                        ),
                    ),
                    DesignItem(
                        2,
                        FoodItem(
                            1,
                            "Одинокий бургер",
                            105.9F,
                            "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                            false,
                        ),
                        Style(
                            x = 0.9F,
                            y = 0.1F,
                            scale = 1.1F,
                        ),
                    ),
                    DesignItem(
                        3,
                        FoodItem(
                            1,
                            "Самый крутой бургер",
                            105.9F,
                            "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                            true,
                        ),
                        Style(
                            x = 0.8F,
                            y = 0.8F,
                            scale = 2F,
                            foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                        ),
                    ),
                    DesignItem(
                        4,
                        CategoryWithFoodItems(
                            category = Category(
                                id = 1,
                                name = "Вкусняшки",
                                imageUri = "https://i.pinimg.com/originals/b3/24/cb/b324cbabe17d8913e556722973aa6f09.png",
                                price = 1000.2F,
                                inStock = null,
                            ),
                            foodItems = listOf(
                                FoodItem(
                                    1,
                                    "Бургер",
                                    105.9F,
                                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                                    false,
                                ),
                                FoodItem(
                                    1,
                                    "Пицца",
                                    105.9F,
                                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                                    true,
                                )
                            ),
                        ),
                        Style(
                            x = 0.1F,
                            y = 0.45F,
                            scale = 1.5F,
                            notInStockStyle = NotInStockStyle.CROSSED_OUT,
                            showPrice = true,
                            categoryItemStyle = Style(
                                notInStockStyle = NotInStockStyle.CROSSED_OUT
                            )
                        ),
                    ),
                    DesignItem(
                        5,
                        ImageItem(
                            1,
                            "https://yt3.googleusercontent.com/ytc/AIdro_kRmcneiyQOhH6n0KVn7FgbCkAfh0AI0EA9RpgzBnHq3g=s900-c-k-c0x00ffffff-no-rj"
                        ),
                        Style(
                            x = 0.4F,
                            y = 0.85F,
                            scale = 1.7F,
                        ),
                    ),
                )
            ),
            TVScreenWithDesignItems(
                TVScreen(
                    0,
                    "Третий экран",
                    800,
                    600,
                    ScreenStyle(),
                ),
                designItems = emptyList()
            ),
            TVScreenWithDesignItems(
                TVScreen(
                    0,
                    "Четвёртый экран",
                    1600,
                    1000,
                    ScreenStyle(),
                ),
                designItems = emptyList()
            ),
        )
    )
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize().background(colorScheme.background)
        ) {
            VisualConfigurationScreenContent(
                uiState,
                onNavigateToScreen = {},
                onAddScreen = {},
                onToggleDrawer = {},
            )
        }
    }
}


@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VisualConfigurationScreenEmptyPreview() {
    val uiState = VisualConfigurationScreenUiState(
        screens = emptyList()
    )
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize().background(colorScheme.background)
        ) {
            VisualConfigurationScreenContent(
                uiState,
                onNavigateToScreen = {},
                onAddScreen = {},
                onToggleDrawer = {},
            )
        }
    }
}