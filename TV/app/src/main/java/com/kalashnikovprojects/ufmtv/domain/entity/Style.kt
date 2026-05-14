package com.kalashnikovprojects.ufmtv.domain.entity

data class Style(
    val x: Int,
    val y: Int,
    val scale: Float,
    val notInStockStyle: NotInStockStyle?,
    val textColorHex: String?,
    val showImage: Boolean?,
    val showPrice: Boolean?,
)
