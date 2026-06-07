package com.example.ufmcontroller.presentation.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ufmcontroller.R
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppTop(
    text: String="Universal Food Menu",
    onButton: () -> Unit,
    buttonIsToMenu: Boolean = true,
    title: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onButton,
                colors = IconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = colorScheme.onBackground,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = colorScheme.onBackground.copy(alpha = 0.5F),
                ),
                modifier = Modifier.size(60.dp)
            ) {
                if (buttonIsToMenu) {
                    Icon(Icons.Filled.Menu,
                        contentDescription = "Menu")
                } else {
                    Icon(Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Back")
                }
            }
        },
        title = title ?: @Composable {
            Text(
                text,
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = actions ?: @Composable {
            Icon(
                painter = painterResource(id = R.drawable.ufm_icon_foreground),
                contentDescription = "UFM logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(60.dp)
            )
        },
        modifier = Modifier.padding(bottom = 5.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun DefaultAppTopPreview() {
    UFMControllerTheme {
        DefaultAppTop(onButton = {})
    }
}