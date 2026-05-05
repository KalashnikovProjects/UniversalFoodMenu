package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class TVScreen(
    val id: Int,
    val name: String,
    val width: Int,
    val height: Int,
)

fun TVScreen.toNoIdTVScreen() = NoIdTVScreen(
    name = name,
    width = width,
    height = height,
)

@Serializable
data class NoIdTVScreen(
    val name: String,
    val width: Int,
    val height: Int,
)

fun NoIdTVScreen.toTVScreen(id: Int) = TVScreen(
    id = id,
    name = name,
    width = width,
    height = height,
)