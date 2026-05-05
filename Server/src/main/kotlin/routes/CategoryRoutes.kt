package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.eventbus.EventBus
import com.kalashnikovprojects.ufmserver.data.repository.CategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsCategoriesRepository
import com.kalashnikovprojects.ufmserver.dto.Events
import com.kalashnikovprojects.ufmserver.dto.FoodItem
import com.kalashnikovprojects.ufmserver.dto.NoIdCategory
import com.kalashnikovprojects.ufmserver.dto.toCategory
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlin.getValue


fun Route.categoryRoutes() {
    val categoriesRepository by inject<CategoriesRepository>()
    val foodItemsCategoriesRepository by inject<FoodItemsCategoriesRepository>()

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

            val request = call.receive<List<FoodItem>>()

            try {
                foodItemsCategoriesRepository.setFoodItemsForCategory(userId,
                    id,
                    request.map { it.id }
                )
                eventBus.getFlow(userId).emit(Events.SetCategoryItems(
                    id,
                    request
                ))
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage ?: "Unknown error")
            }
        }

        post("/categories") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val request = call.receive<NoIdCategory>()

            try {
                val createdId = categoriesRepository.create(userId, request)
                eventBus.getFlow(userId).emit(Events.AddCategoryEvent(
                    element = request.toCategory(createdId)
                ))
                call.respond(HttpStatusCode.Created, createdId)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage ?: "Unknown error")
            }
        }

        put("/categories/{id}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val request = call.receive<NoIdCategory>()
            val isUpdated = categoriesRepository.updateById(userId, id, request)

            if (isUpdated) {
                eventBus.getFlow(userId).emit(
                    Events.ChangeCategoryEvent(
                        id,
                        element = request.toCategory(id)
                    )
                )
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

            val isDeleted = categoriesRepository.deleteById(userId, id)
            if (isDeleted) {
                Events.DeleteCategoryEvent(id)
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}