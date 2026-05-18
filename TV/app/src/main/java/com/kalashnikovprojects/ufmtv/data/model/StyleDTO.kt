package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.Style
import kotlinx.serialization.Serializable

@Serializable
data class StyleDTO(
    val x: Int?,
    val y: Int?,
    val scale: Float?,
    val notInStockStyle: NotInStockStyleDTO?,
    val textColorHex: String?,
    val showImage: Boolean?,
    val showPrice: Boolean?,
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
        categoryItemStyle=categoryItemStyle?.toEntity(),
    )
}