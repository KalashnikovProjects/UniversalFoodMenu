package com.kalashnikovprojects.ufmtv.data.remote

import android.content.Context
import android.os.Build
import com.kalashnikovprojects.ufmtv.BuildConfig
import com.kalashnikovprojects.ufmtv.data.model.LoginEventsDTO
import com.kalashnikovprojects.ufmtv.domain.entity.LoginEvents
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context,
    private val client: HttpClient,
    ) {
    private val ipAddress: String = BuildConfig.SERVER_IP
    private val port: Int = BuildConfig.SERVER_PORT

    private val _events = MutableSharedFlow<LoginEvents>()
    val events = _events.asSharedFlow()

    private var connectionJob: Job? = null

    fun connect(scope: CoroutineScope) {

        if (connectionJob?.isActive == true) return

        connectionJob = scope.launch {
            try {
                val metrics = context.resources.displayMetrics
                val width = metrics.widthPixels
                val height = metrics.heightPixels

                client.webSocket(host = ipAddress, port = port, path = "/api/ws/tv_auth",
                    request = {
                        attributes.put(NoTokenRequest, true)
                        url {
                            parameters.append("screen_width", width.toString())
                            parameters.append("screen_height", height.toString())
                            parameters.append("screen_name", Build.MODEL.toString())
                        }
                    }) {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val event = Json.decodeFromString<LoginEventsDTO>(text)
                            _events.emit(event.toEntity())
                        }
                    }
                }
            } catch (e: Exception) {
                _events.emit(LoginEvents.ClosedWithError)
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        connectionJob?.cancel()
    }
}