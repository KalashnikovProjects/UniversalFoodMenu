package com.kalashnikovprojects.ufmserver.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.websocket.WebSockets
import kotlinx.io.files.FileNotFoundException
import kotlin.time.Duration.Companion.seconds


fun Application.configureRateLimit() {
    install(RateLimit) {
        register(RateLimitName("enterScreenAuthCodeRateLimit")) {
            rateLimiter(limit = 10, refillPeriod = 30.seconds)
        }
    }
}