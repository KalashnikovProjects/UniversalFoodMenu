package com.kalashnikovprojects.ufmtv.presentation.ui.component

import androidx.tv.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.kalashnikovprojects.ufmtv.domain.entity.Style
import com.kalashnikovprojects.ufmtv.domain.entity.TextItem

@Composable
fun TextItemDisplay(
    textItem: TextItem,
    style: Style,
) {
    val textColor = if (!style.textColorHex.isNullOrBlank()) {
        val hex = style.textColorHex.removePrefix("#")
        val cleanHex = if (hex.length == 6) "FF$hex" else hex
        Color(cleanHex.toLong(16))
    } else {
        colorScheme.onBackground
    }

    Text(textItem.text,
        fontSize = 14.sp,
        color=textColor,
        textAlign = TextAlign.Center,
    )
}