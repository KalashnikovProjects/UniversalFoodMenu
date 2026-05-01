package com.example.ufmcontroller.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ufmcontroller.domain.model.FoodItem
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import coil.compose.AsyncImage
import com.example.ufmcontroller.presentation.ui.component.FoodItemRowCard

// TODO: добавить во viewModel и в какие-то сохранялки между запусками режим отображения (плитки или список)

@Composable
fun HomeScreen(
    filteredFoodItems: List<FoodItem>,
    onFoodItemToggle: (Int) -> Unit,
    searchState: TextFieldState,
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
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES // Включает темную тему
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