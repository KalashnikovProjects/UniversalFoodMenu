package com.kalashnikovprojects.ufmserver.plugins

import com.kalashnikovprojects.ufmserver.data.repository.ScreensRepository
import io.ktor.server.application.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Application.configureSerialization() {
    val appJson by inject<Json>()

    install(ContentNegotiation) {
        json(appJson)
    }
}