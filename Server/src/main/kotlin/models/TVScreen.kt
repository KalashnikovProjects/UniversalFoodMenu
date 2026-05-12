package com.kalashnikovprojects.ufmserver.models

import kotlinx.serialization.Serializable
import kotlin.String

@Serializable
data class TVScreen(
    val id: Int,
    val name: String,
    val width: Int,
    val height: Int,
    val style: String,
)

fun TVScreen.toNoIdTVScreen() = NoIdTVScreen(
    name = name,
    width = width,
    height = height,
    style=style,
)

@Serializable
data class NoIdTVScreen(
    val name: String,
    val width: Int,
    val height: Int,
    val style: String,
    )

fun NoIdTVScreen.toTVScreen(id: Int) = TVScreen(
    id = id,
    name = name,
    width = width,
    height = height,
    style=style,
    )