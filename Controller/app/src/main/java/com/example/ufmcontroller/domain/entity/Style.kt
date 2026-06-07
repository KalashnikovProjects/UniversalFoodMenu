package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.StyleDTO
import com.example.ufmcontroller.data.model.toEntity

data class Style(
    val x: Float?=null,
    val y: Float?=null,
    val scale: Float?=null,
    val notInStockStyle: NotInStockStyle?=null,
    val textColorHex: String?=null,
    val showImage: Boolean?=null,
    val showPrice: Boolean?=null,
    val foodItemDisplayTypeStyle: FoodItemDisplayTypeStyle?=null,
    val imageScale: Float?=null,
    val itemWidthScale: Float?=null,
    val categoryItemStyle: Style?=null,
)

fun Style.withDefaultStyle(defaultStyle: Style): Style = Style(
    x=(x ?: defaultStyle.x),
    y=(y ?: defaultStyle.y),
    scale=(scale ?: defaultStyle.scale),
    notInStockStyle=(notInStockStyle ?: defaultStyle.notInStockStyle),
    textColorHex=(textColorHex ?: defaultStyle.textColorHex),
    showImage=(showImage ?: defaultStyle.showImage),
    showPrice=(showPrice ?: defaultStyle.showPrice),
    foodItemDisplayTypeStyle=(foodItemDisplayTypeStyle ?: defaultStyle.foodItemDisplayTypeStyle),
    imageScale=(imageScale ?: defaultStyle.imageScale),
    itemWidthScale=(itemWidthScale ?: defaultStyle.itemWidthScale),
    categoryItemStyle=(categoryItemStyle ?: defaultStyle.categoryItemStyle),
    )



fun Style.toDTO() : StyleDTO {
    return StyleDTO(
        x = x,
        y = y,
        scale = scale,
        notInStockStyle = notInStockStyle?.toDTO(),
        textColorHex = textColorHex,
        showImage = showImage,
        showPrice = showPrice,
        foodItemDisplayTypeStyle=foodItemDisplayTypeStyle?.toDTO(),
        imageScale=imageScale,
        itemWidthScale=itemWidthScale,
        categoryItemStyle=categoryItemStyle?.toDTO(),
    )
}