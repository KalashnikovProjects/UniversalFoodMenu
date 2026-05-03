package com.kalashnikovprojects.ufmserver.modules

import com.kalashnikovprojects.ufmserver.routes.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            authorizationRoutes()
            tvAuthorizationRoutes()
        }
        staticResources("/static", "static")
    }
}