package com.kalashnikovprojects.ufmtv.data.remote

import com.kalashnikovprojects.ufmtv.BuildConfig
import com.kalashnikovprojects.ufmtv.data.model.LoginEvent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginWebSocketService @Inject constructor(
    private val client: HttpClient,
    ) {
    private val ipAddress = BuildConfig.SERVER_IP
    private val port = BuildConfig.SERVER_PORT.toInt()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    private var connectionJob: Job? = null

    fun connect(scope: CoroutineScope) {

        if (connectionJob?.isActive == true) return

        connectionJob = scope.launch {
            try {
                client.webSocket(host = ipAddress, port = port, path = "/api/ws/tv_auth") {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val event = Json.decodeFromString<LoginEvent>(text)
                            _events.emit(event)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        connectionJob?.cancel()
    }
}