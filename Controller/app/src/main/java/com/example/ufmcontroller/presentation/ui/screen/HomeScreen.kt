package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.FoodItemRowCard
import com.example.ufmcontroller.presentation.viewmodel.MainScreenUiState
import com.example.ufmcontroller.presentation.viewmodel.MainScreenViewModel

// TODO: добавить во viewModel и в какие-то сохранялки между запусками режим отображения (плитки или список)

@Composable
fun HomeScreen(
    viewModel: MainScreenViewModel= hiltViewModel<MainScreenViewModel>(),
    navigateEditFoodItem: (Int) -> Unit,
    onToggleDrawer: () -> Unit,
) {
    val uiState: MainScreenUiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        searchState = viewModel.searchState,
        navigateEditFoodItem = navigateEditFoodItem,
        toggleFoodItem = {id -> viewModel.toggleFoodItem(id) },
        onToggleDrawer = onToggleDrawer,
    )
}

@Composable
fun HomeScreenContent(
    uiState: MainScreenUiState,
    searchState: TextFieldState,
    navigateEditFoodItem: (Int) -> Unit,
    toggleFoodItem: (Int) -> Unit,
    onToggleDrawer: () -> Unit,
) {
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
            items(uiState.filteredFoodItems) {
                    item ->
                Box(modifier = Modifier.combinedClickable(
                    onClick = { },
                    onLongClick = {
                        navigateEditFoodItem(item.id)
                    },
                )) {
                    FoodItemRowCard(item = item, { toggleFoodItem(item.id) })
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true, showBackground = true)
@Preview(name = "Dark Mode", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    val searchState by remember { mutableStateOf(TextFieldState("")) }
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize().background(colorScheme.background)
        ) {
            HomeScreenContent(
                uiState = MainScreenUiState(
                    filteredFoodItems = listOf(
                        FoodItem(
                            1,
                            "Американский бургер",
                            imageUri = "https://static.vecteezy.com/system/resources/previews/041/290/624/non_2x/ai-generated-fresh-burger-isolated-on-transparent-background-free-png.png",
                            price = 99.99F,
                            inStock = true
                        ),
                        FoodItem(
                            2,
                            "Итальянская пицца",
                            imageUri = null,
                            price = 99.99F,
                            inStock = false
                        ),
                        FoodItem(
                            2,
                            "Китайский вок",
                            imageUri = null,
                            price = 99.99F,
                            inStock = false
                        ),
                        FoodItem(
                            1,
                            "Японские роллы",
                            imageUri = null,
                            price = 99.99F,
                            inStock = true
                        )
                    )
                ),
                searchState = searchState,
                navigateEditFoodItem = {},
                toggleFoodItem = { },
                onToggleDrawer = { },
            )
        }
    }
}