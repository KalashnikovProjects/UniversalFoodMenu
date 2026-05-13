package com.kalashnikovprojects.ufmtv.data.remote

import com.kalashnikovprojects.ufmtv.data.model.Events
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventWebSocketService @Inject constructor(
    private val client: HttpClient
) {
    private val _events = MutableSharedFlow<Events>()
    val events = _events.asSharedFlow()

    private var connectionJob: Job? = null

    fun connect(scope: CoroutineScope) {
        if (connectionJob?.isActive == true) return

        connectionJob = scope.launch {
        }
    }

    fun disconnect() {
        connectionJob?.cancel()
    }
}