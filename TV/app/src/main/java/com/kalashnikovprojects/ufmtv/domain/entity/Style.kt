package com.kalashnikovprojects.ufmtv.domain.entity

data class Style(
    val x: Int?,
    val y: Int?,
    val scale: Float?,
    val notInStockStyle: NotInStockStyle?,
    val textColorHex: String?,
    val showImage: Boolean?,
    val showPrice: Boolean?,
    val categoryItemStyle: Style?,
)

fun defaultStyle(): Style = Style(
    x=null,
    y=null,
    scale=null,
    notInStockStyle=null,
    textColorHex=null,
    showImage=null,
    showPrice=null,
    categoryItemStyle=null
)