package com.kalashnikovprojects.ufmtv.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.presentation.theme.UFMControllerTheme
import com.kalashnikovprojects.ufmtv.presentation.ui.component.FoodItemRowCard
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.MainMenuViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun MainMenuScreen(
    viewModel: MainMenuViewModel = hiltViewModel<MainMenuViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startUpdating()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(
            top = 10.dp, start=5.dp, end=5.dp, bottom=20.dp)) {
            IconButton(onClick = onToggleDrawer,
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Settings")
            }
            BasicTextField(
                state = searchState,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(6.dp),
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),
                decorator = { innerTextField ->
                    Box {
                        if (searchState.text.isEmpty()) {
                            Text(
                                "Поиск...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                }
            )

            IconButton(onClick = {
                // TODO: переключить режим отображения
                },
                modifier = Modifier.wrapContentSize(),
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "Table view")
            }
        }
        LazyColumn {
            items(filteredFoodItems) {
                FoodItemRowCard(item = it, onFoodItemToggle)
            }
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true, showBackground = true)
@Preview(
    name = "Dark Mode",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES // Включает темную тему
)
@Composable
fun HomeScreenPreview() {
    UFMControllerTheme {
        HomeScreen(
            filteredFoodItems = listOf(
                FoodItem(1, "Американский бургер", imageUri = "https://static.vecteezy.com/system/resources/previews/041/290/624/non_2x/ai-generated-fresh-burger-isolated-on-transparent-background-free-png.png", price = 99.99F, inStock = true),
                FoodItem(2, "Итальянская пицца", imageUri = null, price = 99.99F, inStock = false),
                FoodItem(2, "Китайский вок", imageUri = null, price = 99.99F, inStock = false),
                FoodItem(1, "Японские роллы", imageUri = null, price = 99.99F, inStock = true)),
            onFoodItemToggle = {},
            searchState = TextFieldState(""),
            onToggleDrawer={}
        )
    }
}