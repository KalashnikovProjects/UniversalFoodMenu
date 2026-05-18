package com.kalashnikovprojects.ufmtv.domain.entity

data class TVScreen(
    val id: Int,
    val name: String,
    val width: Int,
    val height: Int,
    val style: ScreenStyle,
)

fun defaultTVScreen() = TVScreen(
    id = 0,
    name = "",
    width = 0,
    height = 0,
    style=defaultScreenStyle(),
)