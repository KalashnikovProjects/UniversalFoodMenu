package com.kalashnikovprojects.ufmtv.domain.entity

data class ScreenStyle(
    val screenTheme: ScreenTheme?,
    val backgroundColorHex: String?,
    val defaultStyle: Style?,
)

fun defaultScreenStyle(): ScreenStyle {
    return ScreenStyle (
        screenTheme = null,
        backgroundColorHex = null,
        defaultStyle = defaultStyle(),
    )
}