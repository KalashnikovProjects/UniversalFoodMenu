package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.usecase.login.SengLoginUseCase
import com.example.ufmcontroller.domain.usecase.login.SengRegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class LoginTab {
    LOGIN, REGISTER
}

data class LoginUiState (
    val currentTab: LoginTab = LoginTab.LOGIN,
    val passwordHidden: Boolean = true,
    val step: LoginStep = LoginStep.Normal,
)

sealed interface LoginStep {
    object Normal: LoginStep
    object Loading: LoginStep
    data class Error(val message: String) : LoginStep
    object Successful : LoginStep
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: SengLoginUseCase,
    private val registerUseCase: SengRegisterUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
    val loginFieldState: TextFieldState = TextFieldState()
    val passwordFieldState: TextFieldState = TextFieldState()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                step = LoginStep.Loading
            )
            try {
                loginUseCase(username, password)
                _uiState.value = _uiState.value.copy(
                    step = LoginStep.Successful
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    step = LoginStep.Error(e.message ?: "Неизвестная ошибка")
                )
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                step = LoginStep.Loading
            )
            try {
                registerUseCase(username, password)
                _uiState.value = _uiState.value.copy(
                    step = LoginStep.Successful
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    step = LoginStep.Error(e.message ?: "Неизвестная ошибка")
                )
            }
        }
    }

    fun selectTab(tab: LoginTab) {
        _uiState.value = _uiState.value.copy(
            currentTab = tab,
        )
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            passwordHidden = !_uiState.value.passwordHidden,
        )
    }
}