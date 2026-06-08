package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.usecase.screen.InputCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AddTvScreenUiState (
    val step: AddTvScreenStep = AddTvScreenStep.Normal,
)

sealed interface AddTvScreenStep {
    object Normal: AddTvScreenStep
    object Loading: AddTvScreenStep
    data class Error(val message: String) : AddTvScreenStep
    data class Successful(val id: Int) : AddTvScreenStep
}

@HiltViewModel
class AddTvScreenViewModel @Inject constructor(
    private val inputCodeUseCase: InputCodeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddTvScreenUiState())
    val uiState = _uiState.asStateFlow()
    val codeFieldState: TextFieldState = TextFieldState()

    fun postCode() {
        if (codeFieldState.text.length != 6) {
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                step = AddTvScreenStep.Loading
            )
            try {
                val new = inputCodeUseCase(codeFieldState.text.toString())
                _uiState.value = _uiState.value.copy(
                    step = AddTvScreenStep.Successful(new.id)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    step = AddTvScreenStep.Error(e.message ?: "Неизвестная ошибка")
                )
            }
        }
    }
}