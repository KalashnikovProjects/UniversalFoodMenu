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
    width = 1,
    height = 1,
    style= ScreenStyle(),
)

fun TVScreen.toDTO() = TVScreen(
    id = id,
    name = name,
    width = width,
    height = height,
    style = style
)