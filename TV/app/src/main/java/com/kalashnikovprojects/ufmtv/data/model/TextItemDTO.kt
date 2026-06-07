package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.TextItem
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