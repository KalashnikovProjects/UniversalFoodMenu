package com.kalashnikovprojects.ufmtv.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme.colorScheme
import androidx.tv.material3.Text
import androidx.tv.material3.surfaceColorAtElevation

@Composable
fun LoginScreenLoadingWithText(
    text: String,
) {
    LoginScreenBase {
        Text(text,
            fontSize = 18.sp.scaled(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500,
            modifier = Modifier.width(550.dp.scaled()),
            lineHeight = 20.sp.scaled(),
            color=colorScheme.onBackground,)
        Spacer(modifier = Modifier.padding(vertical = 12.dp.scaled()))
        Box(modifier = Modifier
            .size(90.dp.scaled())
            .clip(RoundedCornerShape(20.dp.scaled()))
            .background(colorScheme.surfaceColorAtElevation(3.dp))
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize().padding(18.dp.scaled()),
                color = colorScheme.secondary,
                trackColor = colorScheme.surfaceVariant,
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 19.dp.scaled()))
    }
}