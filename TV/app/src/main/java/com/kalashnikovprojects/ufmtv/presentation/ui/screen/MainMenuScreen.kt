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
import com.kalashnikovprojects.ufmtv.presentation.ui.component.ScreenDisplay
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
    ScreenDisplay(uiState.screenStyle, uiState.designItems)
}