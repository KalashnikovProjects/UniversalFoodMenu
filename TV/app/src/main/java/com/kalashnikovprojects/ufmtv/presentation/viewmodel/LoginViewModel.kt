package com.kalashnikovprojects.ufmtv.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalashnikovprojects.ufmtv.domain.usecase.StartListeningLoginUseCase
import com.kalashnikovprojects.ufmtv.domain.entity.LoginEvents
import com.kalashnikovprojects.ufmtv.domain.usecase.FinishLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUIState(
    val loginStep: LoginStep,
)

sealed interface LoginStep {
    object Initial : LoginStep
    data class DisplayCode(val code: String) : LoginStep
    object Completed : LoginStep
    object ReconnectBecauseError : LoginStep

}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val startListeningLoginUseCase: StartListeningLoginUseCase,
    private val finishLoginUseCase: FinishLoginUseCase,
    ) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUIState>(LoginUIState(
        loginStep = LoginStep.Initial
    ))
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()
    private var isListeningStarted = false

    private val _navigationEvent = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun startListeningLoginEvents() {
        if (isListeningStarted) return
        isListeningStarted = true

        viewModelScope.launch {
            startListeningLoginUseCase().collect { event ->
                when (event) {
                    is LoginEvents.CodeReceived -> {
                        _uiState.update { currentState ->
                            currentState.copy(loginStep = LoginStep.DisplayCode(event.code))
                        }
                    }
                    is LoginEvents.TokenReceived -> {
                        _uiState.update { currentState ->
                            currentState.copy(loginStep = LoginStep.Completed)
                        }
                        _navigationEvent.emit(Unit)
                    }
                    is LoginEvents.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(loginStep = LoginStep.ReconnectBecauseError)
                        }
                    }
                }
            }
        }
    }

    fun stopListeningLoginEvents() {
        if (!isListeningStarted) return
        isListeningStarted = false

        viewModelScope.launch {
            finishLoginUseCase()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopListeningLoginEvents()
    }
}