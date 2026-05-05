package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.EventBus.EventBus
import com.kalashnikovprojects.ufmserver.adapters.filestorage.FileStorageAdapter
import com.kalashnikovprojects.ufmserver.data.repository.CategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsCategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsRepository
import com.kalashnikovprojects.ufmserver.dto.Category
import com.kalashnikovprojects.ufmserver.dto.Event
import com.kalashnikovprojects.ufmserver.dto.NoIdCategory
import com.kalashnikovprojects.ufmserver.dto.NoIdFoodItem
import com.kalashnikovprojects.ufmserver.dto.toCategory
import com.kalashnikovprojects.ufmserver.dto.toFoodItem
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.foodItemsRoutes() {
    val foodItemsRepository by inject<FoodItemsRepository>()
    val foodItemsCategoriesRepository by inject<FoodItemsCategoriesRepository>()

    val fileStorageAdapter by inject<FileStorageAdapter>()

    val eventBus by inject<EventBus>()

    authenticate {
        get("/food-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val items = foodItemsRepository.getAllByUserId(userId)
            call.respond(HttpStatusCode.OK, items)
        }

        get("/food-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val item = foodItemsRepository.getById(userId, id)
            if (item != null) {
                call.respond(HttpStatusCode.OK, item)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/food-items/{id}/categories") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val items = foodItemsCategoriesRepository.getCategoriesForFoodItem(userId, id)
            call.respond(HttpStatusCode.OK, items)
        }

        put("/food-items/{id}/categories") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val request = call.receive<List<Category>>()

            try {
                foodItemsCategoriesRepository.setCategoriesForFoodItem(userId,
                    id,
                    request.map { it.id }
                )
                eventBus.getFlow(userId).emit(Event.SetFoodCategories(
                    id,
                    request
                ))
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage ?: "Unknown error")
            }
        }

        post("/food-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()
            val request = call.receive<NoIdFoodItem>()
            try {
                val multipart = call.receiveMultipart()
                var fileBytes: ByteArray? = null
                var fileName = ""
                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        fileBytes = part.streamProvider().readBytes()
                        fileName = part.originalFileName as String
                    }
                    part.dispose()
                }
                fileBytes?.let { bytes ->
                    val imageUrl = fileStorageAdapter.saveFoodItemImage(bytes, fileName.substringAfterLast(".", ""))
                    request.imageUri = imageUrl
                }

                val createdId = foodItemsRepository.create(userId, request)
                eventBus.getFlow(userId).emit(Event.AddFoodEvent(
                    element = request.toFoodItem(createdId)
                ))
                call.respond(HttpStatusCode.Created, request.toFoodItem(createdId))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        put("/food-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }
            try {
                val request = call.receive<NoIdFoodItem>()
                val multipart = call.receiveMultipart()
                var fileBytes: ByteArray? = null
                var fileName = ""
                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        fileBytes = part.streamProvider().readBytes()
                        fileName = part.originalFileName as String
                    }
                    part.dispose()
                }
                fileBytes?.let { bytes ->
                    val imageUrl = fileStorageAdapter.saveFoodItemImage(
                        bytes,
                        fileName.substringAfterLast(".", "")
                    )
                    request.imageUri = imageUrl
                }

                val isUpdated = foodItemsRepository.updateById(userId, id, request)

                if (isUpdated) {
                    eventBus.getFlow(userId).emit(
                        Event.ChangeFoodEvent(
                            id,
                            element = request.toFoodItem(id)
                        )
                    )
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        post("/food-items/{id}/toggle") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@post
            }

            val inStock = call.receive<Boolean>()
            val isUpdated = foodItemsRepository.toggleById(userId, id, inStock)

            if (isUpdated) {
                eventBus.getFlow(userId).emit(Event.ToggleFoodEvent(id, inStock))
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/food-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            val isDeleted = foodItemsRepository.deleteById(userId, id)
            if (isDeleted) {
                eventBus.getFlow(userId).emit(Event.DeleteFoodEvent(
                    id,
                ))
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}