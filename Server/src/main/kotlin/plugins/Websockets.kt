package com.kalashnikovprojects.ufmserver.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.serialization.kotlinx.*
import io.ktor.server.websocket.WebSockets
import kotlinx.serialization.json.*

fun Application.configureWebsockets() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
}