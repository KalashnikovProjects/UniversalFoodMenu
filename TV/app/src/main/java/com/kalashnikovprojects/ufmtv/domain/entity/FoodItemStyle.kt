package com.kalashnikovprojects.ufmtv.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface FoodItemStyle {
    data class Row(
        val beforePricePadding: Float?=null
    ): FoodItemStyle

    object Cell: FoodItemStyle
}