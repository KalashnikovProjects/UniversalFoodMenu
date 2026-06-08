package com.example.ufmcontroller.data.remote

import android.util.Log
import com.example.ufmcontroller.BuildConfig
import com.example.ufmcontroller.data.local.UserPreferencesDataSource
import com.example.ufmcontroller.data.model.EventsDTO
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.wss
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class EventsWebSocketService @Inject constructor(
    private val client: HttpClient,
    private val userPreferencesDataSource: UserPreferencesDataSource,
    ) {
    val logoutEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _events = MutableSharedFlow<EventsDTO>()
    val events = _events.asSharedFlow()

    private var connectionJob: Job? = null

    fun connect(scope: CoroutineScope) {
        if (connectionJob?.isActive == true) return

        if (BuildConfig.DEBUG) {
            Log.d("UFM", "Is debug")
        } else {
            Log.d("UFM", "is release")
        }

        Log.d("UFM", "connected_events")
        connectionJob = scope.launch {
            var currentDelay = 500L
            val maxDelay = 10000L

            while (isActive) {
                try {
                    client.wss(
                        path = "/api/ws/updates") {
                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                val event = Json.decodeFromString<EventsDTO>(text)
                                _events.emit(event)
                            }
                        }
                    }
                } catch (e: WebSocketException) {
                    if (e.message?.contains("401") == true) {
                        Log.d("UFM", "WebSocket Auth Error: 401 Unauthorized")
                        userPreferencesDataSource.clearAuthToken()
                        logoutEvent.emit(Unit)
                        break
                    } else {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    delay(currentDelay.milliseconds)
                    currentDelay = (currentDelay * 2).coerceAtMost(maxDelay)
                }
            }
        }
    }

    fun disconnect() {
        Log.d("UFM", "Disconnect events webSocket")
        connectionJob?.cancel()
        connectionJob = null
    }
}