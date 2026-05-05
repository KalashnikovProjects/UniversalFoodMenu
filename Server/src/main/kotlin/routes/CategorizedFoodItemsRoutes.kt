package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.data.repository.CategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsCategoriesRepository
import com.kalashnikovprojects.ufmserver.dto.CategorizedResponse
import com.kalashnikovprojects.ufmserver.dto.toCategoryWithFoodItems
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.categorizedFoodItemsRoutes() {
    val categoriesRepository by inject<CategoriesRepository>()
    val foodItemsCategoriesRepository by inject<FoodItemsCategoriesRepository>()

    authenticate {
        get("/categorized-food-items") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()

            val categories = categoriesRepository.getAllByUserId(userId).map {
                val foodItems = foodItemsCategoriesRepository.getFoodItemsForCategory(userId, it.id)
                it.toCategoryWithFoodItems(foodItems)
            }
            val noCategoryFoodItems = foodItemsCategoriesRepository.getNoCategoryFoodItems(userId)
            val response = CategorizedResponse(
                categories,
                noCategoryFoodItems
            )
            call.respond(HttpStatusCode.OK, response)
        }
    }
}