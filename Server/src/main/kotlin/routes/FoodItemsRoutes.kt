package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.eventbus.EventBus
import com.kalashnikovprojects.ufmserver.adapters.filestorage.FileStorageAdapter
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsCategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsRepository
import com.kalashnikovprojects.ufmserver.models.Category
import com.kalashnikovprojects.ufmserver.models.Events
import com.kalashnikovprojects.ufmserver.models.NoIdFoodItem
import com.kalashnikovprojects.ufmserver.models.toFoodItem
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
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.foodItemsRoutes() {
    val foodItemsRepository by inject<FoodItemsRepository>()
    val foodItemsCategoriesRepository by inject<FoodItemsCategoriesRepository>()

    val fileStorageAdapter by inject<FileStorageAdapter>()
    val appJson by inject<Json>()

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

            val request = call.receive<List<Int>>()
            foodItemsCategoriesRepository.setCategoriesForFoodItem(userId,
                id,
                request
            )
            eventBus.getFlow(userId).emit(Events.SetFoodCategories(
                id,
                request
            ))
            call.respond(HttpStatusCode.OK)
        }

        post("/food-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            var requestPayload: NoIdFoodItem? = null
            var fileBytes: ByteArray? = null
            var fileName = ""

            val multipart = call.receiveMultipart()

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "foodItemData") {
                            requestPayload = appJson.decodeFromString<NoIdFoodItem>(part.value)
                        }
                    }
                    is PartData.FileItem -> {
                        if (part.name == "image") {
                            fileBytes = part.streamProvider().readBytes()
                            fileName = part.originalFileName ?: "unknown.jpg"
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }

            if (requestPayload == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing food item data")
                return@post
            }

            fileBytes?.let { bytes ->
                val extension = fileName.substringAfterLast(".", "")
                val imageUrl = fileStorageAdapter.saveFoodItemImage(bytes, extension)
                requestPayload!!.imageUri = imageUrl
            }
            val createdId = foodItemsRepository.create(userId, requestPayload!!)
            val finalFoodItem = requestPayload!!.toFoodItem(createdId)

            eventBus.getFlow(userId).emit(Events.AddFoodEvent(element = finalFoodItem))
            call.respond(HttpStatusCode.Created, finalFoodItem)
        }

        put("/food-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            var requestPayload: NoIdFoodItem? = null
            var fileBytes: ByteArray? = null
            var fileName = ""

            val multipart = call.receiveMultipart()

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "foodItemData") {
                            requestPayload = appJson.decodeFromString<NoIdFoodItem>(part.value)
                        }
                    }
                    is PartData.FileItem -> {
                        if (part.name == "image") {
                            fileBytes = part.streamProvider().readBytes()
                            fileName = part.originalFileName ?: "unknown.jpg"
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }

            if (requestPayload == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing food item data")
                return@put
            }

            fileBytes?.let { bytes ->
                val extension = fileName.substringAfterLast(".", "")
                val imageUrl = fileStorageAdapter.saveFoodItemImage(bytes, extension)
                requestPayload!!.imageUri = imageUrl
            }

            val isUpdated = foodItemsRepository.updateById(userId, id, requestPayload)

            if (isUpdated) {
                eventBus.getFlow(userId).emit(
                    Events.ChangeFoodEvent(
                        id,
                        element = requestPayload.toFoodItem(id)
                    )
                )
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
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
                eventBus.getFlow(userId).emit(Events.ToggleFoodEvent(id, inStock))
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
            val foodItem = foodItemsRepository.getById(userId, id)
            if (foodItem == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            val isDeleted = foodItemsRepository.deleteById(userId, id)
            if (isDeleted) {
                eventBus.getFlow(userId).emit(Events.DeleteFoodEvent(
                    id,
                ))
                foodItem.imageUri?.let {
                    fileStorageAdapter.deleteFoodItemImage(foodItem.imageUri)
                }

                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}