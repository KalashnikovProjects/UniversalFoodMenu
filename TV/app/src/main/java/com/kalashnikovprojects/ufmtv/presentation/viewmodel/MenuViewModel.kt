package com.kalashnikovprojects.ufmtv.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalashnikovprojects.ufmtv.domain.usecase.GetDesignItemsUseCase
import com.kalashnikovprojects.ufmtv.domain.usecase.GetScreenStyleUseCase
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MainMenuUIState(
    val designItems: List<DesignItem>,
    val screenStyle: ScreenStyle,
)

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val getDesignItemsUseCase: GetDesignItemsUseCase,
    private val getScreenStyleUseCase: GetScreenStyleUseCase,
    ) : ViewModel() {
    val uiState: StateFlow<MainMenuUIState> = combine(
        getDesignItemsUseCase(),
        getScreenStyleUseCase(),
    ) { designItems, screenStyle ->
        MainMenuUIState(
            designItems = designItems,
            screenStyle = screenStyle,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainMenuUIState(
            designItems = emptyList(),
            screenStyle = ScreenStyle(),
        )
    )
}