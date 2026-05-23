package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.Style
import kotlinx.serialization.Serializable

@Serializable
data class StyleDTO(
    val x: Float?,
    val y: Float?,
    val scale: Float?,
    val notInStockStyle: NotInStockStyleDTO?,
    val textColorHex: String?,
    val showImage: Boolean?,
    val showPrice: Boolean?,
    val foodItemDisplayTypeStyle: FoodItemDisplayTypeStyleDTO?,
    val imageScale: Float?,
    val itemWidthScale: Float?,
    val categoryItemStyle: StyleDTO?,
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