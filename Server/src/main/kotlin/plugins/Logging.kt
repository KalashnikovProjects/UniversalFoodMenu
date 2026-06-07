package com.kalashnikovprojects.ufmserver.plugins

import com.kalashnikovprojects.ufmserver.adapters.jwt.JwtConfig
import com.kalashnikovprojects.ufmserver.data.DbConfig
import com.kalashnikovprojects.ufmserver.di.getAppModule
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging
import org.slf4j.event.*

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO
    }
}