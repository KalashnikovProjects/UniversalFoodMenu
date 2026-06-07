package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.eventbus.EventBus
import com.kalashnikovprojects.ufmserver.adapters.filestorage.FileStorageAdapter
import com.kalashnikovprojects.ufmserver.data.repository.CategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsCategoriesRepository
import com.kalashnikovprojects.ufmserver.models.Events
import com.kalashnikovprojects.ufmserver.models.FoodItem
import com.kalashnikovprojects.ufmserver.models.NoIdCategory
import com.kalashnikovprojects.ufmserver.models.NoIdFoodItem
import com.kalashnikovprojects.ufmserver.models.toCategory
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


fun Route.categoryRoutes() {
    val categoriesRepository by inject<CategoriesRepository>()
    val foodItemsCategoriesRepository by inject<FoodItemsCategoriesRepository>()

    val fileStorageAdapter by inject<FileStorageAdapter>()
    val appJson by inject<Json>()

    val eventBus by inject<EventBus>()

    authenticate {
        get("/categories") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val items = categoriesRepository.getAllByUserId(userId)
            call.respond(HttpStatusCode.OK, items)
        }

        get("/categories/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val item = categoriesRepository.getById(userId, id)
            if (item != null) {
                call.respond(HttpStatusCode.OK, item)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/categories/{id}/food-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val items = foodItemsCategoriesRepository.getFoodItemsForCategory(userId, id)
            call.respond(HttpStatusCode.OK, items)
        }

        put("/categories/{id}/food-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val request = call.receive<List<Int>>()
            foodItemsCategoriesRepository.setFoodItemsForCategory(userId,
                id,
                request
            )
            eventBus.getFlow(userId).emit(Events.SetCategoryItems(
                id,
                request
            ))
            call.respond(HttpStatusCode.OK)
        }

        post("/categories") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            var requestPayload: NoIdCategory? = null
            var fileBytes: ByteArray? = null
            var fileName = ""

            val multipart = call.receiveMultipart()

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "categoryData") {
                            requestPayload = appJson.decodeFromString<NoIdCategory>(part.value)
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
                call.respond(HttpStatusCode.BadRequest, "Missing category data")
                return@post
            }

            fileBytes?.let { bytes ->
                val extension = fileName.substringAfterLast(".", "")
                val imageUrl = fileStorageAdapter.saveCategoryItemImage(bytes, extension)
                requestPayload!!.imageUri = imageUrl
            }

            val createdId = categoriesRepository.create(userId, requestPayload)
            eventBus.getFlow(userId).emit(Events.AddCategoryEvent(
                element = requestPayload.toCategory(createdId)
            ))
            call.respond(HttpStatusCode.Created, requestPayload.toCategory(createdId))
        }

        put("/categories/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            var requestPayload: NoIdCategory? = null
            var fileBytes: ByteArray? = null
            var fileName = ""

            val multipart = call.receiveMultipart()

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "categoryData") {
                            requestPayload = appJson.decodeFromString<NoIdCategory>(part.value)
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
                val imageUrl = fileStorageAdapter.saveCategoryItemImage(bytes, extension)
                requestPayload!!.imageUri = imageUrl
            }
            val isUpdated = categoriesRepository.updateById(userId, id, requestPayload)

            if (isUpdated) {
                eventBus.getFlow(userId).emit(
                    Events.ChangeCategoryEvent(
                        id,
                        element = requestPayload.toCategory(id)
                    )
                )
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post("/categories/{id}/toggle") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@post
            }

            val inStock = call.receive<Boolean>()
            val isUpdated = categoriesRepository.toggleById(userId, id, inStock)

            if (isUpdated) {
                eventBus.getFlow(userId).emit(Events.ToggleCategoryEvent(id, inStock))
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        delete("/categories/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }
            val categoryItem = categoriesRepository.getById(userId, id)
            if (categoryItem == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }

            val isDeleted = categoriesRepository.deleteById(userId, id)
            if (isDeleted) {
                categoryItem.imageUri?.let {
                    fileStorageAdapter.deleteCategoryItemImage(categoryItem.imageUri)
                }
                eventBus.getFlow(userId).emit(
                    Events.DeleteCategoryEvent(id)
                )
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}