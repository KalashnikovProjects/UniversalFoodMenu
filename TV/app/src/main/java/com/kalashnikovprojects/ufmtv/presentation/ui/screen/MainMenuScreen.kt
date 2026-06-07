package com.kalashnikovprojects.ufmtv.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme.colorScheme
import androidx.tv.material3.Text
import com.kalashnikovprojects.ufmtv.presentation.ui.component.ScreenDisplay
import com.kalashnikovprojects.ufmtv.presentation.ui.component.scaled
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
    if (uiState.designItems.isNotEmpty()) {
        ScreenDisplay(uiState.screenStyle, uiState.designItems)
    } else {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Text("Добавьте элементы для отображения в настройках экрана на телефоне",
                fontSize = 23.sp.scaled(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W500,
                modifier = Modifier.width(550.dp.scaled()),
                lineHeight = 23.sp.scaled(),
                color=colorScheme.onBackground
            )
        }
    }
}