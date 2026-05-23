package com.example.ufmcontroller.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ufmcontroller.domain.usecase.login.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


data class SettingsUiState(
    val dropdownOpen: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    val uiState = MutableStateFlow(SettingsUiState())

    fun logoutBlocking() {
        runBlocking {
            logoutUseCase()
        }
    }

    fun toggleDropdown() {
        uiState.value = uiState.value.copy(
            dropdownOpen = !uiState.value.dropdownOpen
        )
    }
}