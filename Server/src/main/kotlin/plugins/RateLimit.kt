package com.kalashnikovprojects.ufmserver.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import kotlin.time.Duration.Companion.seconds


fun Application.configureRateLimit() {
    install(RateLimit) {
        register(RateLimitName("enterScreenAuthCodeRateLimit")) {
            rateLimiter(limit = 10, refillPeriod = 30.seconds)
        }
    }
}