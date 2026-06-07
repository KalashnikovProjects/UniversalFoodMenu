package com.example.ufmcontroller.data.remote

import android.util.Log
import com.example.ufmcontroller.BuildConfig
import com.example.ufmcontroller.data.local.UserPreferencesDataSource
import com.example.ufmcontroller.data.model.Events
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
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
class EventsWebSocketService @Inject constructor(
    private val client: HttpClient,
    private val userPreferencesDataSource: UserPreferencesDataSource,
    ) {
    val logoutEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _events = MutableSharedFlow<Events>()
    val events = _events.asSharedFlow()

    private var connectionJob: Job? = null

    fun connect(scope: CoroutineScope) {
        if (BuildConfig.DEBUG) {
            Log.d("UFM", "Is debug")
        } else {
            Log.d("UFM", "is release")
        }

        if (connectionJob?.isActive == true) return
        Log.d("UFM", "connected_events")
        connectionJob = scope.launch {
            try {
                client.webSocket(path = "/api/ws/updates") {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val event = Json.decodeFromString<Events>(text)
                            _events.emit(event)
                        }
                    }
                }
            } catch (e: WebSocketException) {
                if (e.message?.contains("401") == true) {
                    Log.w("UFM", "WebSocket Auth Error: 401 Unauthorized")
                    userPreferencesDataSource.clearAuthToken()
                    logoutEvent.emit(Unit)
                } else {
                    e.printStackTrace()
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