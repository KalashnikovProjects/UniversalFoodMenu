package com.kalashnikovprojects.ufmtv.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalashnikovprojects.ufmtv.domain.usecase.GetDesignItemsUseCase
import com.kalashnikovprojects.ufmtv.domain.usecase.GetScreenStyleUseCase
import com.kalashnikovprojects.ufmtv.domain.model.DesignItem
import com.kalashnikovprojects.ufmtv.domain.model.ScreenStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    val getDesignItemsUseCase: GetDesignItemsUseCase,
    val getScreenStyleUseCase: GetScreenStyleUseCase,
    ) : ViewModel() {
    data class MainMenuUIState(
        var designItems: List<DesignItem>,
        var screenStyle: ScreenStyle,
    )
    private val _uiState = MutableStateFlow(MainMenuUIState(
        designItems = emptyList(),
        screenStyle = ScreenStyle(
            backgroundColorHex = null,
            defaultNotInStockStyle = null,
            defaultTextColorHex = null,
            defaultShowPrice = null,
        )
    ))
    val uiState: StateFlow<MainMenuUIState> = _uiState


    fun startUpdating() {
        viewModelScope.launch {
            getDesignItemsUseCase().collect { items ->
                _uiState.value.designItems = items
            }
        }
        viewModelScope.launch {
            getScreenStyleUseCase().collect { style ->
                _uiState.value.screenStyle = style
            }
        }
    }
}