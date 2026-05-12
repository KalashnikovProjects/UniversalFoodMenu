package com.kalashnikovprojects.ufmserver.plugins

import com.kalashnikovprojects.ufmserver.data.repository.CategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.DesignItemsRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsCategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsRepository
import com.kalashnikovprojects.ufmserver.data.repository.ImageItemsRepository
import com.kalashnikovprojects.ufmserver.data.repository.Repository
import com.kalashnikovprojects.ufmserver.data.repository.ScreensRepository
import com.kalashnikovprojects.ufmserver.data.repository.TextItemsRepository
import com.kalashnikovprojects.ufmserver.data.repository.UsersRepository
import io.ktor.server.application.Application
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.koin.ktor.ext.inject

fun Application.configureDatabase() {
    val usersRepository by inject<UsersRepository>()
    val screensRepository by inject<ScreensRepository>()
    val foodItemsRepository by inject<FoodItemsRepository>()
    val categoriesRepository by inject<CategoriesRepository>()
    val foodItemsCategoriesRepository by inject<FoodItemsCategoriesRepository>()
    val imageItemsRepository by inject<ImageItemsRepository>()
    val textItemsRepository by inject<TextItemsRepository>()
    val designItemsRepository by inject<DesignItemsRepository>()

    kotlinx.coroutines.runBlocking {
        listOf<Repository>(
            usersRepository,
            screensRepository,
            foodItemsRepository,
            categoriesRepository,
            foodItemsCategoriesRepository,
            imageItemsRepository,
            textItemsRepository,
            designItemsRepository,
        ).map {
            async {
                it.createSchema()
            }
        }.awaitAll()
    }
}