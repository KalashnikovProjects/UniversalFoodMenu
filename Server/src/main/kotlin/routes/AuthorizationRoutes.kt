package com.kalashnikovprojects.ufmserver.routes

import com.kalashnikovprojects.ufmserver.adapters.hashing.HashingAdapter
import com.kalashnikovprojects.ufmserver.adapters.jwt.JwtAdapter
import com.kalashnikovprojects.ufmserver.data.repository.UsersRepository
import com.kalashnikovprojects.ufmserver.models.UserRawPassword
import com.kalashnikovprojects.ufmserver.models.NoIdUserHashedPassword
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Route.authorizationRoutes() {
    val userRepository by inject<UsersRepository>()
    val hashingAdapter by inject<HashingAdapter>()
    val jwtAdapter by inject<JwtAdapter>()

    post("/register") {
        val request = call.receive<UserRawPassword>()
        val passwordHash = hashingAdapter.hashPassword(request.rawPassword)
        try {
            val id = userRepository.create(
                NoIdUserHashedPassword(request.username, passwordHash)
            )
            val token = jwtAdapter.generateJwtToken(id, request.username)
            call.respondText(
                token,
                status=HttpStatusCode.Created
            )
        } catch (_: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.Conflict,
                "User with username '${request.username}' already exists"
            )
        } catch (_: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                "Unknown error"
            )
        }
    }

    post("/login") {
        val request = call.receive<UserRawPassword>()
        val user = userRepository.getByUsername(request.username)
        if (user == null ||
            !hashingAdapter.checkPassword(request.rawPassword, user.hashedPassword)) {
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }
        val token = jwtAdapter.generateJwtToken(user.id, user.username)
        call.respondText(token, status=HttpStatusCode.OK)
    }
}