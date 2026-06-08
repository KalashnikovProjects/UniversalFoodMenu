package com.kalashnikovprojects.ufmserver.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging
import org.slf4j.event.*

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
    }
}