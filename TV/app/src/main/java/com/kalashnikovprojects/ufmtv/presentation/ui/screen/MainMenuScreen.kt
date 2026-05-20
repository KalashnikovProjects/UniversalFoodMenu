package com.kalashnikovprojects.ufmtv.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Devices.TV_720p
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalashnikovprojects.ufmtv.domain.entity.Category
import com.kalashnikovprojects.ufmtv.domain.entity.CategoryWithFoodItems
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItemDisplayTypeStyle
import com.kalashnikovprojects.ufmtv.domain.entity.ImageItem
import com.kalashnikovprojects.ufmtv.domain.entity.NotInStockStyle
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenTheme
import com.kalashnikovprojects.ufmtv.domain.entity.Style
import com.kalashnikovprojects.ufmtv.domain.entity.TextItem
import com.kalashnikovprojects.ufmtv.domain.entity.withDefaultStyle
import com.kalashnikovprojects.ufmtv.presentation.theme.backgroundDark
import com.kalashnikovprojects.ufmtv.presentation.theme.backgroundLight
import com.kalashnikovprojects.ufmtv.presentation.ui.component.CategoryDisplay
import com.kalashnikovprojects.ufmtv.presentation.ui.component.FoodItemDisplay
import com.kalashnikovprojects.ufmtv.presentation.ui.component.ImageItemDisplay
import com.kalashnikovprojects.ufmtv.presentation.ui.component.TextItemDisplay
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.MainMenuUIState
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.MainMenuViewModel

@Composable
fun MainMenuScreen(
    onNavigateLoginScreen: () -> Unit,
    viewModel: MainMenuViewModel = hiltViewModel<MainMenuViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        viewModel.startUpdating()
        onDispose {
            viewModel.stopUpdating()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigateLoginScreenEvent.collect { event ->
            onNavigateLoginScreen()
        }
    }

    MainMenuContent(uiState)
}

@Composable
fun MainMenuContent(uiState: MainMenuUIState) {

    val themeBackgroundColor = if (uiState.screenStyle.screenTheme==ScreenTheme.WHITE)
        backgroundLight
    else backgroundDark
    val backgroundColor = if (uiState.screenStyle.backgroundColorHex != null)
        Color(uiState.screenStyle.backgroundColorHex.toLong(16))
    else themeBackgroundColor
    Box (
        modifier = Modifier.fillMaxSize().background(backgroundColor),
    ) {
        uiState.designItems.forEach { (id, element, style) ->
            key(id) {
                val finalStyle = if (uiState.screenStyle.defaultStyle != null)
                    style.withDefaultStyle(uiState.screenStyle.defaultStyle)
                else style

                val biasX = ((style.x ?: 0.5F) * 2) - 1f
                val biasY = ((style.y ?: 0.5F) * 2) - 1f
                Box(
                    modifier = Modifier
                        .align(BiasAlignment(biasX, biasY))
                        .scale(style.scale ?: uiState.screenStyle.defaultStyle?.scale?: 0.5F)
                ) {
                    when (element) {
                        is FoodItem -> FoodItemDisplay(
                            element,
                            finalStyle
                        )
                        is CategoryWithFoodItems -> CategoryDisplay(
                            element,
                            finalStyle,
                            uiState.screenStyle.defaultStyle?: Style(),
                        )
                        is ImageItem -> ImageItemDisplay(
                            element,
                            finalStyle
                        )
                        is TextItem -> TextItemDisplay(
                            element,
                            finalStyle
                        )
                    }
                }
            }
        }
    }
}

@Preview(name="1080pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_1080p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name="720pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_720p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MeinMenuPreview() {
    MainMenuContent(
        uiState = MainMenuUIState(
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
                            notInStockStyle = NotInStockStyle.CROSSED_OUT)
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
            ),
            screenStyle = ScreenStyle(),
        )
    )
}
