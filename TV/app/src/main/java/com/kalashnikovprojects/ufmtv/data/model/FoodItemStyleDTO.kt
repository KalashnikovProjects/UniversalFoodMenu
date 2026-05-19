package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.FoodItemStyle
import com.kalashnikovprojects.ufmtv.domain.entity.LoginEvents
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface FoodItemStyleDTO {
    fun toEntity(): FoodItemStyle

    @SerialName("row")
    @Serializable
    data class Row(
        val beforePricePadding: Float?
    ): FoodItemStyleDTO {
        override fun toEntity() = FoodItemStyle.Row(beforePricePadding)
    }

    @SerialName("row")
    @Serializable
    object Cell: FoodItemStyleDTO {
        override fun toEntity() = FoodItemStyle.Cell
    }
}