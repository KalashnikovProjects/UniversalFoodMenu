package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.data.repository.ImageItemsRepository
import com.kalashnikovprojects.ufmserver.dto.NoIdImageItem
import com.kalashnikovprojects.ufmserver.dto.toImageItem

import com.kalashnikovprojects.ufmserver.adapters.eventbus.EventBus
import com.kalashnikovprojects.ufmserver.adapters.filestorage.FileStorageAdapter
import com.kalashnikovprojects.ufmserver.dto.Events
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlin.getValue


fun Route.imageItemsRoutes() {
    val imageItemsRepository by inject<ImageItemsRepository>()
    val fileStorageAdapter by inject<FileStorageAdapter>()

    val eventBus by inject<EventBus>()

    authenticate {
        get("/image-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val items = imageItemsRepository.getAllByUserId(userId)
            call.respond(HttpStatusCode.OK, items)
        }

        get("/image-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val item = imageItemsRepository.getById(userId, id)
            if (item != null) {
                call.respond(HttpStatusCode.OK, item)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post("/image-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()
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
            if (fileBytes == null) {
                call.respond(HttpStatusCode.BadRequest, "No file uploaded")
                return@post
            }
            val imageUrl = fileStorageAdapter.saveImageItemImage(fileBytes, fileName.substringAfterLast(".", ""))
            val image = NoIdImageItem(imageUrl)

            val createdId = imageItemsRepository.create(userId, image)
            eventBus.getFlow(userId).emit(Events.AddImageEvent(
                element = image.toImageItem(createdId)
            ))
            call.respond(HttpStatusCode.Created, image.toImageItem(createdId))
        }

        delete("/image-items/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }
            val imageItem = imageItemsRepository.getById(userId, id)
            if (imageItem == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            val isDeleted = imageItemsRepository.deleteById(userId, id)
            if (isDeleted) {
                fileStorageAdapter.deleteImageItemImage(imageItem.imageUri)
                eventBus.getFlow(userId).emit(Events.DeleteImageEvent(
                    id,
                ))
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}