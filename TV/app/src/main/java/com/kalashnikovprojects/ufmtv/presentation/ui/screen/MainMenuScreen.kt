package com.kalashnikovprojects.ufmtv.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalashnikovprojects.ufmtv.domain.entity.Category
import com.kalashnikovprojects.ufmtv.domain.entity.CategoryWithFoodItems
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.ImageItem
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenTheme
import com.kalashnikovprojects.ufmtv.domain.entity.Style
import com.kalashnikovprojects.ufmtv.domain.entity.TextItem
import com.kalashnikovprojects.ufmtv.presentation.theme.backgroundDark
import com.kalashnikovprojects.ufmtv.presentation.theme.backgroundLight
import com.kalashnikovprojects.ufmtv.presentation.ui.component.FoodItemDisplay
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
                val biasX = ((style.x ?: 0.5F) * 2) - 1f
                val biasY = ((style.y ?: 0.5F) * 2) - 1f

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .align(BiasAlignment(biasX, biasY))
                        .scale(style.scale ?: uiState.screenStyle.defaultStyle?.scale?: 0.5F)
                ) {
                    when (element) {
                        is FoodItem -> FoodItemDisplay(
                            element,
                            style
                        )
                        is CategoryWithFoodItems -> TODO()
                        is ImageItem -> TODO()
                        is TextItem -> TODO()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MeinMenuPreview1() {
    MainMenuContent(
        // TODO заполнить сюда ещё больще всего
        uiState = MainMenuUIState(
            designItems = listOf(
                DesignItem(
                    1,
                    CategoryWithFoodItems(
                        category = Category(
                            id = 1,
                            name = "Бургеры",
                            imageUri = null,
                            price = null,
                            inStock = null,
                        ),
                        foodItems = emptyList(),
                    ),
                    Style(
                        x = 0.5F,
                        y = 0.3F,
                        scale = 1.1F,
                    ),
                )
            ),
            screenStyle = ScreenStyle(),
        )
    )
}