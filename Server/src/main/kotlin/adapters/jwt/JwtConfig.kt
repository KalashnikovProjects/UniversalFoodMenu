package com.kalashnikovprojects.ufmserver.adapters.jwt

data class JwtConfig (
    val jwtDomain: String,
    val jwtRealm: String,
    val jwtSecret: String,
)