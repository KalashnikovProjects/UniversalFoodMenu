package com.example.ufmcontroller.domain.entity

data class TVScreen(
    val id: Int=0,
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
    style= ScreenStyle(),
)

fun TVScreen.toDTO() = TVScreen(
    id = id,
    name = name,
    width = width,
    height = height,
    style = style
)