package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.defaultTVScreen
import com.example.ufmcontroller.domain.usecase.login.SengLoginUseCase
import com.example.ufmcontroller.domain.usecase.login.SengRegisterUseCase
import com.example.ufmcontroller.domain.usecase.screen.GetScreenUseCase
import com.example.ufmcontroller.domain.usecase.screen.InputCodeUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class TvScreenUiState (
    val screen: TVScreen,
    val selected: Int?,
)

// TODO. Тут ничего не готово
@HiltViewModel(assistedFactory = TvScreenViewModel.Factory::class)
class TvScreenViewModel @AssistedInject constructor(
    private val getScreenUseCase: GetScreenUseCase,
    @Assisted private val id: Int
) : ViewModel() {

    private val _selectedId = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<TvScreenUiState> = combine(
        getScreenUseCase(id),
        _selectedId
    ) { screen, selectedId ->
        TvScreenUiState(
            screen = screen,
            selected = selectedId
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TvScreenUiState(
            screen = defaultTVScreen(),
            selected = null,
        )
    )

    fun selectElement(id: Int?) {
        _selectedId.update { id }
    }

    @AssistedFactory
    interface Factory {
        fun create(id: Int): TvScreenViewModel
    }
}