package com.kalashnikovprojects.ufmtv.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalashnikovprojects.ufmtv.presentation.ui.component.ScreenDisplay
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