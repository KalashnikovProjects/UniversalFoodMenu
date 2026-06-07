package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.DesignableDTO
import com.example.ufmcontroller.data.model.TextItemDTO

data class TextItem(
    val id: Int=0,
    val text: String,
) : Designable {
    override fun toDTO(): DesignableDTO {
        return TextItemDTO(
            id=id,
            text = text
        )
    }
}