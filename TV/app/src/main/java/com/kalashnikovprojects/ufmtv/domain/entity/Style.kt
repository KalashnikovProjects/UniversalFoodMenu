package com.kalashnikovprojects.ufmtv.domain.entity

data class Style(
    val x: Float?=null,
    val y: Float?=null,
    val scale: Float?=null,
    val notInStockStyle: NotInStockStyle?=null,
    val textColorHex: String?=null,
    val showImage: Boolean?=null,
    val showPrice: Boolean?=null,
    val foodItemStyle: FoodItemStyle?=null,
    val imageScale: Float?=null,
    val categoryItemStyle: Style?=null,
)