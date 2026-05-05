package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserHashedPassword(
    val id: Int,
    val username: String,
    val hashedPassword: String
)

fun UserHashedPassword.toNoIdUserHashedPassword() = NoIdUserHashedPassword(
    username=username,
    hashedPassword=hashedPassword,
)

@Serializable
data class NoIdUserHashedPassword(
    val username: String,
    val hashedPassword: String
)

fun NoIdUserHashedPassword.toUserHashedPassword(id: Int) = UserHashedPassword(
    id = id,
    username=username,
    hashedPassword=hashedPassword,
)