package com.kalashnikovprojects.ufmtv.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalashnikovprojects.ufmtv.domain.usecase.GetDesignItemsUseCase
import com.kalashnikovprojects.ufmtv.domain.usecase.GetScreenStyleUseCase
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.entity.defaultScreenStyle
import com.kalashnikovprojects.ufmtv.domain.usecase.FinishListeningUpdatesUseCase
import com.kalashnikovprojects.ufmtv.domain.usecase.GetLogoutEventsUseCase
import com.kalashnikovprojects.ufmtv.domain.usecase.StartListeningUpdatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainMenuUIState(
    val designItems: List<DesignItem>,
    val screenStyle: ScreenStyle,
)

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val startListeningUpdatesUseCase: StartListeningUpdatesUseCase,
    private val finishListeningUpdatesUseCase: FinishListeningUpdatesUseCase,
    private val getDesignItemsUseCase: GetDesignItemsUseCase,
    private val getScreenStyleUseCase: GetScreenStyleUseCase,
    private val getLogoutEventsUseCase: GetLogoutEventsUseCase,
    ) : ViewModel() {

    val navigateLoginScreenEvent = getLogoutEventsUseCase()

    private val _uiState = MutableStateFlow(MainMenuUIState(
        designItems = emptyList(),
        screenStyle = defaultScreenStyle(),
    ))
    val uiState: StateFlow<MainMenuUIState> = _uiState
    private var isUpdatingStarted = false

    fun startUpdating() {
        if (isUpdatingStarted) return
        isUpdatingStarted = true

        viewModelScope.launch {
            startListeningUpdatesUseCase()
            launch {
                getDesignItemsUseCase().collect { items ->
                    _uiState.update { currentState ->
                        currentState.copy(designItems = items)
                    }
                }
            }
            launch {
                getScreenStyleUseCase().collect { style ->
                    _uiState.update { currentState ->
                        currentState.copy(screenStyle = style)
                    }
                }
            }
        }
    }

    fun stopUpdating() {
        if (!isUpdatingStarted) return
        isUpdatingStarted = false

        viewModelScope.launch(NonCancellable) {
            finishListeningUpdatesUseCase()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdating()
    }
}