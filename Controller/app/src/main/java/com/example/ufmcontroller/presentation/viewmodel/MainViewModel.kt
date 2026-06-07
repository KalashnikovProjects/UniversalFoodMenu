package com.example.ufmcontroller.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ufmcontroller.domain.usecase.service.GetLogoutEventUseCase
import com.example.ufmcontroller.domain.usecase.service.StartServiceUseCase
import com.example.ufmcontroller.domain.usecase.service.StopServiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val startServiceUseCase: StartServiceUseCase,
    private val stopServiceUseCase: StopServiceUseCase,
    private val getLogoutEventUseCase: GetLogoutEventUseCase,
) : ViewModel() {
    val logoutEvent: SharedFlow<Unit> = getLogoutEventUseCase()

    fun startEventsService() {
        startServiceUseCase()
    }

    fun stopEventsService() {
        stopServiceUseCase()
    }
}