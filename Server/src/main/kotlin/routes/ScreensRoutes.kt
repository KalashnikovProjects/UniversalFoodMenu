package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.eventbus.EventBus
import com.kalashnikovprojects.ufmserver.adapters.jwt.JwtAdapter
import com.kalashnikovprojects.ufmserver.data.repository.ScreensRepository
import com.kalashnikovprojects.ufmserver.dto.Events
import com.kalashnikovprojects.ufmserver.dto.NoIdTVScreen
import com.kalashnikovprojects.ufmserver.dto.NoIdTextItem
import com.kalashnikovprojects.ufmserver.dto.TVScreen
import com.kalashnikovprojects.ufmserver.dto.toTVScreen
import com.kalashnikovprojects.ufmserver.dto.toTextItem
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.send
import kotlinx.coroutines.CompletableDeferred
import org.koin.ktor.ext.inject
import java.util.concurrent.ConcurrentHashMap
import kotlin.getValue
import kotlin.text.toInt
import kotlin.text.toIntOrNull

fun Route.screensRoutes() {
    val screensRepository by inject<ScreensRepository>()

    val eventBus by inject<EventBus>()

    get("/screens") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("id").asInt()

        val items = screensRepository.getAllByUserId(userId)
        call.respond(HttpStatusCode.OK, items)
    }

    get("/screens/{id}") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("id").asInt()

        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return@get
        }

        val item = screensRepository.getById(userId, id)
        if (item != null) {
            call.respond(HttpStatusCode.OK, item)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post("/screens") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("id").asInt()
        val request = call.receive<NoIdTVScreen>()
        val createdId = screensRepository.create(userId, request)
        eventBus.getFlow(userId).emit(Events.AddScreenEvent(
            element = request.toTVScreen(createdId)
        ))
        call.respond(HttpStatusCode.Created, request.toTVScreen(createdId))
    }

    put("/screens/{id}") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("id").asInt()
        val request = call.receive<NoIdTVScreen>()

        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return@put
        }

        val isUpdated = screensRepository.updateById(userId, id, request)

        if (isUpdated) {
            eventBus.getFlow(userId).emit(Events.ChangeScreenEvent(
                id,
                element = request.toTVScreen(id)
            ))
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    delete("/screens/{id}") {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("id").asInt()

        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            return@delete
        }

        val isDeleted = screensRepository.deleteById(userId, id)
        if (isDeleted) {
            eventBus.getFlow(userId).emit(Events.DeleteScreenEvent(
                id,
            ))
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}