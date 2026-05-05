package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.EventBus.EventBus
import com.kalashnikovprojects.ufmserver.data.repository.ScreensRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.websocketListenersRoutes() {
    val eventBus by inject<EventBus>()

    authenticate {
        webSocket("/ws/updates") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            eventBus.getFlow(userId).collect { event ->
                sendSerialized(event)
            }
        }
    }
}