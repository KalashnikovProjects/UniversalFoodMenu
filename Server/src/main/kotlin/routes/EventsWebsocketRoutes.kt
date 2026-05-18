package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.eventbus.EventBus
import com.kalashnikovprojects.ufmserver.data.repository.CategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.DesignItemsRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsRepository
import com.kalashnikovprojects.ufmserver.data.repository.ImageItemsRepository
import com.kalashnikovprojects.ufmserver.data.repository.ScreensRepository
import com.kalashnikovprojects.ufmserver.data.repository.TextItemsRepository
import com.kalashnikovprojects.ufmserver.models.Events
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Route.eventsWebsocketRoutes() {
    val designItemsRepository by inject<DesignItemsRepository>()
    val textItemsRepository by inject<TextItemsRepository>()
    val foodItemsRepository by inject<FoodItemsRepository>()
    val categoriesRepository by inject<CategoriesRepository>()
    val imageItemsRepository by inject<ImageItemsRepository>()
    val screensRepository by inject<ScreensRepository>()

    val eventBus by inject<EventBus>()

    authenticate {
        webSocket("/ws/updates") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()
            val screenId: Int? = call.request.queryParameters["screen_id"]?.toIntOrNull()
            if (screenId != null) {
                val screen = screensRepository.getById(userId, screenId)
                if (screen == null) {
                    sendSerialized(Events.LogoutScreenEvent(
                        id = screenId,
                    ))
                    return@webSocket
                }
                sendSerialized(Events.ReloadScreen(
                    screenId = screenId,
                    screen = screen,
                ))

                sendSerialized(Events.ReloadDesignItemsByScreenId(
                    screenId = screenId,
                    items = designItemsRepository.getAllByScreenIdUserId(screenId, userId)
                ))
            } else {
                val screensDeferred = async {
                    sendSerialized(Events.ReloadScreens(items = screensRepository.getAllByUserId(userId)))
                }
                val secondDeferred = async {
                    sendSerialized(Events.ReloadFoodItems(items = foodItemsRepository.getAllByUserId(userId)))
                    sendSerialized(Events.ReloadCategoryItems(items = categoriesRepository.getAllByUserId(userId)))
                }
                val textItemsDeferred = async {
                    sendSerialized(Events.ReloadTextItems(items = textItemsRepository.getAllByUserId(userId)))
                }
                val imageItemsDeferred = async {
                    sendSerialized(Events.ReloadImageItems(items = imageItemsRepository.getAllByUserId(userId)))
                }
                awaitAll(screensDeferred, secondDeferred, textItemsDeferred, imageItemsDeferred)
                sendSerialized(Events.ReloadDesignItemsWithScreenId(items = designItemsRepository.getAllByUserId(userId)))
            }

            eventBus.getFlow(userId).collect { event ->
                sendSerialized(event)
            }
        }
    }
}