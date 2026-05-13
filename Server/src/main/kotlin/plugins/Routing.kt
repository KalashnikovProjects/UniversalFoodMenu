package com.kalashnikovprojects.ufmserver.plugins

import com.kalashnikovprojects.ufmserver.routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            authorizationRoutes()
            tvAuthorizationRoutes()
            eventsWebsocketRoutes()
            categoryRoutes()
            foodItemsRoutes()
            categorizedFoodItemsRoutes()
            imageItemsRoutes()
            textItemsRoutes()
            imageItemsRoutes()
            designItemsRoutes()
            screensRoutes()
        }
        staticResources("/static", "static")
    }
}