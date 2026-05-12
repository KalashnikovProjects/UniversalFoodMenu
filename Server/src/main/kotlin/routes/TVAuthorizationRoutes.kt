package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.jwt.JwtAdapter
import com.kalashnikovprojects.ufmserver.data.repository.ScreensRepository
import com.kalashnikovprojects.ufmserver.models.NoIdTVScreen
import com.kalashnikovprojects.ufmserver.models.TVScreen
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.send
import kotlinx.coroutines.CompletableDeferred
import org.koin.ktor.ext.inject
import java.util.concurrent.ConcurrentHashMap
import kotlin.getValue

fun Route.tvAuthorizationRoutes() {
    val screensRepository by inject<ScreensRepository>()

    val jwtAdapter by inject<JwtAdapter>()

    val tvAuthStates = ConcurrentHashMap<Int, suspend (Int, String) -> TVScreen>()

    webSocket("/ws/tv_auth") {
        val screenWidth: Int
        val screenHeight: Int
        val screenName: String

        try {
            screenWidth = call.request.queryParameters["screen_width"]!!.toInt()
            screenHeight = call.request.queryParameters["screen_height"]!!.toInt()
            screenName = call.request.queryParameters["screen_name"]!!
        } catch (_: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Need valid screen_width, screen_height and screen_name params in URL")
            return@webSocket
        }

        var code: Int
        do {
            code = (100000..999999).random()
        } while (tvAuthStates.containsKey(code))

        send(code.toString())

        val authDeferred = CompletableDeferred<Pair<Int, String>>()

        var id: Int = -1
        tvAuthStates[code] = callback@{ userIdReceived, usernameReceived ->
            id = screensRepository.create(
                userIdReceived,
                NoIdTVScreen(
                    screenName,
                    screenWidth,
                    screenHeight,
                    "",
                )
            )
            authDeferred.complete(userIdReceived to usernameReceived)
            return@callback TVScreen(
                id,
                screenName,
                screenWidth,
                screenHeight,
                "",
            )
        }

        try {
            val (userId, username) = authDeferred.await()
            val token = jwtAdapter.generateJwtToken(
                userId,
                username,
            )
            sendSerialized(
                mapOf(
                    "status" to "success",
                    "id" to id,
                    "token" to token
                )
            )

        } catch (_: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
            return@webSocket
        } finally {
            tvAuthStates.remove(code)
        }
    }

    authenticate {
        rateLimit(RateLimitName("enterScreenAuthCodeRateLimit")) {
            post("/input_code_for_tv_auth") {
                val code = call.receive<Int>()
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("id").asInt()
                val username = principal.payload.getClaim("username").asString()

                val callback = tvAuthStates[code]
                if (callback != null) {
                    callback(userId, username)

                    call.respond(
                        HttpStatusCode.OK,
                        mapOf("status" to "success")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Bad code"
                    )
                }
            }
        }
    }
}