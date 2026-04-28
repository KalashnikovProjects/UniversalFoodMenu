package com.example.ufmcontroller.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ufmcontroller.domain.model.FoodItem

// TODO: добавить во viewModel и в какие-то сохранялки между запусками режим отображения (плитки или список)

@Composable
fun HomeScreen(
    foodItems: List<FoodItem>,
    onFoodItemToggle: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(10.dp)) {
            IconButton(onClick = {
                // TODO: открыть меню слева
                },
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Settings")
            }

            TextField(
                value = "...",
                modifier = Modifier.weight(1f),
                onValueChange = {},
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
            items(foodItems) {
                var bgColor = if (it.inStock) Color(red = 0,
                    green = 255,
                    blue = 0,
                    alpha = 35) else
                    Color(red = 255,
                        green = 0,
                        blue = 0,
                        alpha = 35)
                Row(
                    modifier = Modifier
                        .clickable {
                            onFoodItemToggle(it.id)
                        }
                        .background(color=bgColor)
                        .padding(vertical = 20.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.AccountCircle,
                        contentDescription = "тут будет нейрокартинка")
                    // TODO: изображение
                    Text(
                        text = it.name,
                        fontSize = 22.sp,
                        modifier = Modifier.weight(1f),
                        textDecoration = if (!it.inStock) TextDecoration.LineThrough else
                            TextDecoration.None,
                    )
                    IconButton(onClick = {
                        // TODO: переключить режим отображения
                        },
                        modifier = Modifier.requiredSize(30.dp)
                    ) {
                        Icon(Icons.Filled.Menu,
                            contentDescription = "move element")
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        foodItems = listOf(
            FoodItem(1, "Американский бургер", true),
            FoodItem(2, "Итальянская пицца", false),
            FoodItem(2, "Китайский вок", false),
            FoodItem(1, "Японские роллы", true),
            ),
        onFoodItemToggle = {}
    )
}