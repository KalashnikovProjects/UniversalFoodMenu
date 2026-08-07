package com.example.ufmcontroller.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.usecase.service.GetLoadedEventUseCase
import com.example.ufmcontroller.domain.usecase.service.GetLogoutEventUseCase
import com.example.ufmcontroller.domain.usecase.service.StartServiceUseCase
import com.example.ufmcontroller.domain.usecase.service.StopServiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val startServiceUseCase: StartServiceUseCase,
    private val stopServiceUseCase: StopServiceUseCase,
    private val getLogoutEventUseCase: GetLogoutEventUseCase,
    private val getLoadedEventUseCase: GetLoadedEventUseCase,

    ) : ViewModel() {
    private val _uiLogoutEvent = Channel<Unit>(
        capacity = Channel.BUFFERED
    )
    val uiLogoutEvent = _uiLogoutEvent.receiveAsFlow()
    private val _uiLoadedEvent = Channel<Unit>(
        capacity = Channel.BUFFERED
    )
    val uiLoadedEvent = _uiLoadedEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            getLogoutEventUseCase().collect {
                _uiLogoutEvent.send(Unit)
            }
        }
        viewModelScope.launch {
            getLoadedEventUseCase().collect {
                _uiLoadedEvent.send(Unit)
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