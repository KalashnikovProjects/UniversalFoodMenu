package com.kalashnikovprojects.ufmtv.data.remote

import android.util.Log
import com.kalashnikovprojects.ufmtv.BuildConfig
import com.kalashnikovprojects.ufmtv.data.local.UserPreferencesDataSource
import com.kalashnikovprojects.ufmtv.data.model.EventsDTO
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
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
    val loadedEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _events = MutableSharedFlow<EventsDTO>(extraBufferCapacity = 2)
    val events = _events.asSharedFlow()

    private var connectionJob: Job? = null

    suspend fun logout() {
        logoutEvent.emit(Unit)
        disconnect()
    }

    suspend fun loaded() {
        loadedEvent.emit(Unit)
    }

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
                    val currentToken = userPreferencesDataSource.authToken.firstOrNull()
                    client.wss(
                        path = "/api/ws/updates",
                        request = {
                            if (!currentToken.isNullOrBlank()) {
                                header(HttpHeaders.Authorization, "Bearer $currentToken")
                            }
                            url {
                                parameters.append("screen_id", (userPreferencesDataSource.screenId.value ?: 0).toString())
                            }
                        }) {
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
                        Log.w("UFM", "WebSocket Auth Error: 401 Unauthorized")
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
        connectionJob?.cancel()
        connectionJob = null
    }
}