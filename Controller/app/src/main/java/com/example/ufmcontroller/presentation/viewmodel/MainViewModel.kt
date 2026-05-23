package com.example.ufmcontroller.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ufmcontroller.domain.usecase.service.StartServiceUseCase
import com.example.ufmcontroller.domain.usecase.service.StopServiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val startServiceUseCase: StartServiceUseCase,
    private val stopServiceUseCase: StopServiceUseCase
) : ViewModel() {

    fun startEventsService() {
        startServiceUseCase()
    }

    fun stopEventsService() {
        stopServiceUseCase()
    }
}