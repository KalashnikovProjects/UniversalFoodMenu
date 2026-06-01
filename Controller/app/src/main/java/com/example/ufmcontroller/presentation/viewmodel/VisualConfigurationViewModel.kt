package com.example.ufmcontroller.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.usecase.screen.GetScreensWithDesignItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class VisualConfigurationScreenUiState(
    val screens: List<TVScreenWithDesignItems> = emptyList(),
)

@HiltViewModel
class VisualConfigurationViewModel @Inject constructor(
    getScreensUseCase: GetScreensWithDesignItemsUseCase,
) : ViewModel() {

    private val screensFlow = getScreensUseCase()

    val uiState: StateFlow<VisualConfigurationScreenUiState> = screensFlow
        .map { screens ->
            VisualConfigurationScreenUiState(screens = screens)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = VisualConfigurationScreenUiState()
        )
}