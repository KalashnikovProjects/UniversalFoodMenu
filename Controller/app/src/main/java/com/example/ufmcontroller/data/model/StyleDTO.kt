package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.Style
import kotlinx.serialization.Serializable

@Serializable
data class StyleDTO(
    val x: Float?=null,
    val y: Float?=null,
    val scale: Float?=null,
    val notInStockStyle: NotInStockStyleDTO?=null,
    val textColorHex: String?=null,
    val showImage: Boolean?=null,
    val showPrice: Boolean?=null,
    val foodItemDisplayTypeStyle: FoodItemDisplayTypeStyleDTO?=null,
    val imageScale: Float?=null,
    val itemWidthScale: Float?=null,
    @Serializable(with = StyleFromStringSerializer::class)
    val categoryItemStyle: StyleDTO?=null,
)

fun StyleDTO.toEntity() : Style {
    return Style(
        x = x,
        y = y,
        scale = scale,
        notInStockStyle = notInStockStyle?.toEntity(),
        textColorHex = textColorHex,
        showImage = showImage,
        showPrice = showPrice,
        foodItemDisplayTypeStyle=foodItemDisplayTypeStyle?.toEntity(),
        imageScale=imageScale,
        itemWidthScale=itemWidthScale,
        categoryItemStyle=categoryItemStyle?.toEntity(),
    )
}