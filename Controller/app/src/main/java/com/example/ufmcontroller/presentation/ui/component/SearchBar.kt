package com.example.ufmcontroller.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(state: TextFieldState, modifier: Modifier=Modifier) {
    BasicTextField(
        state = state,
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .background(
                colorScheme.surfaceContainer,
                RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 20.dp, vertical = 11.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = colorScheme.onSurface
        ),
        cursorBrush = SolidColor(colorScheme.onSurface),
        decorator = { innerTextField ->
            Box {
                if (state.text.isEmpty()) {
                    Text(
                        "Поиск...",
                        color = colorScheme.onSurfaceVariant,
                    )
                }
                innerTextField()
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    modifier = Modifier.align(Alignment.CenterEnd),
                    tint = colorScheme.onSecondaryContainer,
                )
            }
        }
    )
}