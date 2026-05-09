package com.kalashnikovprojects.ufmserver.adapters.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*


class JwtAdapter(
    val jwtConfig: JwtConfig
) {
    suspend fun generateJwtToken(
        userId: Int,
        username: String,
        screenId: Int?=null,
    ): String {
        return JWT.create()
            .withIssuer(jwtConfig.jwtDomain)
            .withClaim("id", userId)
            .withClaim("username", username)
            .withClaim("screen_id", screenId ?: -1)
            // .withExpiresAt(Date(System.currentTimeMillis() + 90 * 24 * 60 * 60 * 1000)) // 90 дней жизни токена
            .sign(Algorithm.HMAC256(jwtConfig.jwtSecret))
    }
}

