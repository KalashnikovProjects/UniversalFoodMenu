package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.eventbus.EventBus
import com.kalashnikovprojects.ufmserver.data.repository.DesignItemsRepository
import com.kalashnikovprojects.ufmserver.dto.Event
import com.kalashnikovprojects.ufmserver.dto.NoIdDesignItem
import com.kalashnikovprojects.ufmserver.dto.toDesignItem
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.designItemsRoutes() {
    val designItemsRepository by inject<DesignItemsRepository>()

    val eventBus by inject<EventBus>()

    authenticate {
        get("/screen/{screen_id}/design-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val screenId = call.parameters["screen_id"]?.toIntOrNull()
            if (screenId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val items = designItemsRepository.getAllByScreenIdUserId(screenId, userId)
            call.respond(HttpStatusCode.OK, items)
        }

        get("/design-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val item = designItemsRepository.getById(userId, id)
            if (item != null) {
                call.respond(HttpStatusCode.OK, item)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post("/screen/{screen_id}/design-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val screenId = call.parameters["screen_id"]?.toIntOrNull()
            if (screenId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@post
            }

            val request = call.receive<NoIdDesignItem>()
            try {
                val createdId = designItemsRepository.create(userId, screenId, request)
                eventBus.getFlow(userId).emit(Event.AddDesignEvent(
                    element = request.toDesignItem(createdId)
                ))
                call.respond(HttpStatusCode.Created, request.toDesignItem(createdId))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        put("/design-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()
            val request = call.receive<NoIdDesignItem>()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val isUpdated = designItemsRepository.updateById(userId, id, request)

            if (isUpdated) {
                eventBus.getFlow(userId).emit(Event.ChangeDesignEvent(
                    id,
                    element = request.toDesignItem(id)
                ))
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/design-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            val isDeleted = designItemsRepository.deleteById(userId, id)
            if (isDeleted) {
                eventBus.getFlow(userId).emit(Event.DeleteDesignEvent(
                    id,
                ))
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}