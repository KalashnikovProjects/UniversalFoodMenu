package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.TextItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("text")
data class TextItemDTO(
    val id: Int,
    val text: String,
) : DesignableDTO {
    override fun toEntity(): TextItem {
        return TextItem(
            id = id,
            text=text,
        )
    }
}