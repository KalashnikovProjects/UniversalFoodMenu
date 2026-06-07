package com.example.ufmcontroller.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.usecase.service.GetLogoutEventUseCase
import com.example.ufmcontroller.domain.usecase.service.StartServiceUseCase
import com.example.ufmcontroller.domain.usecase.service.StopServiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val startServiceUseCase: StartServiceUseCase,
    private val stopServiceUseCase: StopServiceUseCase,
    private val getLogoutEventUseCase: GetLogoutEventUseCase,
) : ViewModel() {
    private val _uiLogoutEvent = Channel<Unit>(
        capacity = Channel.BUFFERED
    )
    val uiLogoutEvent = _uiLogoutEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            getLogoutEventUseCase().collect {
                _uiLogoutEvent.send(Unit)
            }
        }
    }

    fun startEventsService() {
        startServiceUseCase()
    }

    fun stopEventsService() {
        stopServiceUseCase()
    }
}