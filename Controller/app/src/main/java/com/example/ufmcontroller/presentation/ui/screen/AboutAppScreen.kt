package com.example.ufmcontroller.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ufmcontroller.domain.model.FoodItem

@Composable
fun AboutAppScreen(
    onToggleDrawer: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(
        top = 10.dp, start=5.dp, end=5.dp, bottom=20.dp)) {
        Row {
            IconButton(onClick = onToggleDrawer,
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Settings")
            }
            Text("Universal food menu",
                color=MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge)
        }
        Column(modifier = Modifier.padding(30.dp)) {
            Text("Разработчик",
                fontSize = 22.sp,
                modifier = Modifier.padding(bottom=10.dp))
            Text(buildAnnotatedString {
                append("Github: ")
                withLink(
                    LinkAnnotation.Url(
                        "https://github.com/KalashnikovProjects",
                        TextLinkStyles(style = SpanStyle(color = Color(0xFF7BB07B), fontWeight=FontWeight.Bold))
                    )
                ) {
                    append("https://github.com/KalashnikovProjects")
                }}
            )
            Text(buildAnnotatedString {
                append("Telegram: ")
                withLink(
                    LinkAnnotation.Url(
                        "https://github.com/KalashnikovProjects",
                        TextLinkStyles(style = SpanStyle(color = Color(0xFF7BB07B), fontWeight=FontWeight.Bold))
                    )
                ) {
                    append("https://t.me/Kalashn11k")
                }}
            )
        }

    }
}