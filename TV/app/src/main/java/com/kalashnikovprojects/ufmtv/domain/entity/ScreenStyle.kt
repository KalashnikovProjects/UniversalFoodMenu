package com.kalashnikovprojects.ufmtv.domain.entity

data class ScreenStyle(
    val backgroundColorHex: String?,
    val defaultNotInStockStyle: NotInStockStyle?,
    val defaultTextColorHex: String?,
    val defaultShowPrice: Boolean?,
)

fun defaultScreenStyle(): ScreenStyle {
    return ScreenStyle (
        backgroundColorHex = null,
        defaultNotInStockStyle = null,
        defaultTextColorHex = null,
        defaultShowPrice = null
    )
}