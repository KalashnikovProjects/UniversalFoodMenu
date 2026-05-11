package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.eventbus.EventBus
import com.kalashnikovprojects.ufmserver.data.repository.TextItemsRepository
import com.kalashnikovprojects.ufmserver.dto.Events
import com.kalashnikovprojects.ufmserver.dto.NoIdTextItem
import com.kalashnikovprojects.ufmserver.dto.toTextItem
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlin.getValue


fun Route.textItemsRoutes() {
    val textItemsRepository by inject<TextItemsRepository>()

    val eventBus by inject<EventBus>()

    authenticate {
        get("/text-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val items = textItemsRepository.getAllByUserId(userId)
            call.respond(HttpStatusCode.OK, items)
        }

        get("/text-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val item = textItemsRepository.getById(userId, id)
            if (item != null) {
                call.respond(HttpStatusCode.OK, item)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post("/text-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()
            val request = call.receive<NoIdTextItem>()
            val createdId = textItemsRepository.create(userId, request)
            eventBus.getFlow(userId).emit(Events.AddTextEvent(
                element = request.toTextItem(createdId)
            ))
            call.respond(HttpStatusCode.Created, request.toTextItem(createdId))
        }

        put("/text-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()
            val request = call.receive<NoIdTextItem>()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val isUpdated = textItemsRepository.updateById(userId, id, request)

            if (isUpdated) {
                eventBus.getFlow(userId).emit(Events.ChangeTextEvent(
                    id,
                    element = request.toTextItem(id)
                ))
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/text-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            val isDeleted = textItemsRepository.deleteById(userId, id)
            if (isDeleted) {
                eventBus.getFlow(userId).emit(Events.DeleteTextEvent(
                    id,
                ))
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}