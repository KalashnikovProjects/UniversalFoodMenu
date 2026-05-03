package com.kalashnikovprojects.ufmserver.data

data class DbConfig(
    val dbHost: String,
    val dbName: String,
    val user: String,
    val password: String
)