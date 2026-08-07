package com.kalashnikovprojects.ufmtv.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalashnikovprojects.ufmtv.domain.usecase.FinishListeningUpdatesUseCase
import com.kalashnikovprojects.ufmtv.domain.usecase.GetLoadedEventUseCase
import com.kalashnikovprojects.ufmtv.domain.usecase.GetLogoutEventUseCase
import com.kalashnikovprojects.ufmtv.domain.usecase.StartListeningUpdatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val startListeningUpdatesUseCase: StartListeningUpdatesUseCase,
    private val finishListeningUpdatesUseCase: FinishListeningUpdatesUseCase,
    private val getLogoutEventUseCase: GetLogoutEventUseCase,
    private val getLoadedEventUseCase: GetLoadedEventUseCase,
    ) : ViewModel() {
    private val _navigateLoginScreenEvent = Channel<Unit>(
        capacity = Channel.BUFFERED
    )
    val navigateLoginScreenEvent = _navigateLoginScreenEvent.receiveAsFlow()
    private val _navigateMenuScreenEvent = Channel<Unit>(
        capacity = Channel.BUFFERED
    )
    val navigateMenuScreenEvent = _navigateMenuScreenEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            getLogoutEventUseCase().collect {
                _navigateLoginScreenEvent.send(Unit)
            }
        }
        viewModelScope.launch {
            getLoadedEventUseCase().collect {
                _navigateMenuScreenEvent.send(Unit)
            }
        }
    }

    private var isUpdatingStarted = false

    fun listenUpdates() {
        if (isUpdatingStarted) return
        isUpdatingStarted = true

        viewModelScope.launch {
            startListeningUpdatesUseCase()
        }
    }

    fun stopListeningUpdates() {
        if (!isUpdatingStarted) return
        isUpdatingStarted = false

        viewModelScope.launch(NonCancellable) {
            finishListeningUpdatesUseCase()
        }
    }
}