package com.kalashnikovprojects.ufmserver.plugins

import io.ktor.server.application.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kalashnikovprojects.ufmserver.data.repository.ScreensRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respond
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Application.configureSecurity() {
    val screensRepository by inject<ScreensRepository>()

    val jwtDomain = environment.config.property("jwt.domain").getString()
    val jwtRealm = environment.config.property("jwt.realm").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()
    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(jwtDomain)
                    .build()
            )

            validate { credential ->
                println(credential.payload.toString())
                val userId = credential.payload.getClaim("id").asInt()
                val screenId = credential.payload.getClaim("screen_id").asInt()

                if (screenId != -1 &&
                    screensRepository.getById(userId, screenId) == null) {
                    return@validate null
                }
                return@validate JWTPrincipal(credential.payload)
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}