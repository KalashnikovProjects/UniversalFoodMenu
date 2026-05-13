package com.kalashnikovprojects.ufmtv.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalashnikovprojects.ufmtv.domain.usecase.LoginUseCase
import com.kalashnikovprojects.ufmtv.domain.model.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RegistrationUiState {
    object Initial : RegistrationUiState
    data class DisplayCode(val code: String) : RegistrationUiState
    object Completed : RegistrationUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Initial)
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        listenToWebSocket()
    }

    private fun listenToWebSocket() {
        viewModelScope.launch {
            loginUseCase().collect { event ->
                when (event) {
                    is LoginEvent.CodeReceived -> {
                        _uiState.value = RegistrationUiState.DisplayCode(event.code)
                    }
                    is LoginEvent.TokenReceived -> {
                        _uiState.value = RegistrationUiState.Completed

                        _navigationEvent.emit(Unit)
                    }
                    is LoginEvent.Closed -> {
                        listenToWebSocket()
                    }
                }
            }
        }
    }
}