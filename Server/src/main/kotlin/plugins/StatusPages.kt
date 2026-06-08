package com.kalashnikovprojects.ufmserver.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.r2dbc.spi.R2dbcDataIntegrityViolationException
import kotlinx.io.files.FileNotFoundException

fun Application.configureStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.TooManyRequests) { call, status ->
            val retryAfter = call.response.headers["Retry-After"]
            call.respondText(text = "429: Too many requests. Wait for $retryAfter seconds.", status = status)
        }
        exception<R2dbcDataIntegrityViolationException> { call, cause ->
            if (cause.sqlState == "23505" || cause.sqlState == "23000") {
                call.respondText(text = "name already exists" , status = HttpStatusCode.Conflict)
            } else {
                call.respondText(text = "db error" , status = HttpStatusCode.InternalServerError)

            }
        }
        exception<Throwable> { call, cause ->
            if(cause is FileNotFoundException) {
                call.respondText(text = "404: $cause" , status = HttpStatusCode.NotFound)
            } else {
                call.application.log.error("500 INTERNAL SERVER ERROR: ${cause.stackTraceToString()}")
                call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
            }
        }
    }
}