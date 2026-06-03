package com.example.ufmcontroller.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ufmcontroller.presentation.viewmodel.TvScreenUiState
import com.example.ufmcontroller.presentation.viewmodel.TvScreenViewModel
import com.example.ufmcontroller.presentation.viewmodel.VisualConfigurationScreenUiState
import com.example.ufmcontroller.presentation.viewmodel.VisualConfigurationViewModel


@Composable
fun TvScreenScreen(
    screenId: Int,
    viewModel: TvScreenViewModel = hiltViewModel(
        creationCallback = { factory: TvScreenViewModel.Factory ->
            factory.create(screenId)
        }
    ),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TvScreenScreenContent(
        uiState,
        onBack,
    )
}

@Composable
fun TvScreenScreenContent(
    uiState: TvScreenUiState,
    onBack: () -> Unit,
) {

}