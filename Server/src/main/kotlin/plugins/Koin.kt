package com.kalashnikovprojects.ufmserver.plugins

import com.kalashnikovprojects.ufmserver.adapters.jwt.JwtConfig
import com.kalashnikovprojects.ufmserver.data.DbConfig
import com.kalashnikovprojects.ufmserver.di.getAppModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        val dbHost = environment.config.property("database.host").getString()
        val dbName = environment.config.property("database.name").getString()
        val dbUser = environment.config.property("database.user").getString()
        val dbPassword = environment.config.property("database.password").getString()

        val jwtDomain = environment.config.property("jwt.domain").getString()
        val jwtRealm = environment.config.property("jwt.realm").getString()
        val jwtSecret = environment.config.property("jwt.secret").getString()
        modules(getAppModule(
            DbConfig(dbHost, dbName, dbUser, dbPassword),
            JwtConfig(jwtDomain, jwtRealm, jwtSecret),
        ))
    }
}