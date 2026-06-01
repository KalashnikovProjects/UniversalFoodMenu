package com.example.ufmcontroller.presentation.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ufmcontroller.R
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme


@Composable
fun DefaultAppTop(text: String="Universal Food Menu", onButton: () -> Unit, buttonIsToMenu: Boolean = true) {
    Row(
        modifier = Modifier.padding(
            top = 10.dp, start = 5.dp, end = 5.dp
        )
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onButton,
            colors = IconButtonColors(
                containerColor = Color.Transparent,
                contentColor = colorScheme.onBackground,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = colorScheme.onBackground.copy(alpha = 0.5F),
            ),
            modifier = Modifier.size(70.dp)
        ) {
            if (buttonIsToMenu) {
                Icon(Icons.Filled.MoreVert,
                    contentDescription = "Menu")
            } else {
                Icon(Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back")
            }
        }
        Text(
            text,
            style = MaterialTheme.typography.titleLarge,
            color = colorScheme.onBackground,
        )

        Icon(
            painter = painterResource(id = R.drawable.ufm_icon_foreground),
            contentDescription = "UFM logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(70.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun DefaultAppTopPreview() {
    UFMControllerTheme {
        DefaultAppTop(onButton = {})
    }
}