package com.kalashnikovprojects.ufmtv.data.remote

import android.util.Log
import com.kalashnikovprojects.ufmtv.BuildConfig
import com.kalashnikovprojects.ufmtv.data.local.UserPreferencesDataSource
import com.kalashnikovprojects.ufmtv.data.model.Events
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.request
import io.ktor.http.HttpStatusCode
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
    private val ipAddress: String = BuildConfig.SERVER_IP
    private val port: Int = BuildConfig.SERVER_PORT

    val logoutEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _events = MutableSharedFlow<Events>()
    val events = _events.asSharedFlow()

    private var connectionJob: Job? = null

    fun connect(scope: CoroutineScope) {
        if (connectionJob?.isActive == true) return
        Log.d("UFM", "connected_events")
        connectionJob = scope.launch {
            try {
                client.webSocket(host = ipAddress, port = port, path = "/api/ws/updates",
                    request = {
                        url {
                            parameters.append("screen_id", (userPreferencesDataSource.screenId.value ?: 0).toString())
                        }
                    }) {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val event = Json.decodeFromString<Events>(text)
                            _events.emit(event)
                        }
                    }
                }
            } catch (e: ResponseException) {
                if (e.response.status == HttpStatusCode.Unauthorized) {
                    userPreferencesDataSource.clearAuthToken()
                    userPreferencesDataSource.clearScreenId()
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