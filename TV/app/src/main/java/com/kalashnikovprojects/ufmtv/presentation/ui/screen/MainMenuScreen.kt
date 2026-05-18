package com.kalashnikovprojects.ufmtv.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.MainMenuViewModel

@Composable
fun MainMenuScreen(
    onNavigateLoginScreen: () -> Unit,
    viewModel: MainMenuViewModel = hiltViewModel<MainMenuViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        viewModel.startUpdating()
        onDispose {
            viewModel.stopUpdating()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigateLoginScreenEvent.collect { event ->
            onNavigateLoginScreen()
        }
    }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        Row(modifier = Modifier.padding(
//            top = 10.dp, start=5.dp, end=5.dp, bottom=20.dp)) {
//            IconButton(onClick = onToggleDrawer,
//            ) {
//                Icon(Icons.Filled.MoreVert, contentDescription = "Settings")
//            }
//            BasicTextField(
//                state = searchState,
//                modifier = Modifier
//                    .weight(1f)
//                    .background(
//                        MaterialTheme.colorScheme.surfaceVariant,
//                        RoundedCornerShape(6.dp),
//                    )
//                    .padding(horizontal = 16.dp, vertical = 12.dp),
//                textStyle = MaterialTheme.typography.bodyLarge.copy(
//                    color = MaterialTheme.colorScheme.onSurface
//                ),
//                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),
//                decorator = { innerTextField ->
//                    Box {
//                        if (searchState.text.isEmpty()) {
//                            Text(
//                                "Поиск...",
//                                color = MaterialTheme.colorScheme.onSurfaceVariant
//                            )
//                        }
//                        innerTextField()
//                    }
//                }
//            )
//
//            IconButton(onClick = {
//                },
//                modifier = Modifier.wrapContentSize(),
//            ) {
//                Icon(Icons.Filled.Menu, contentDescription = "Table view")
//            }
//        }
//        LazyColumn {
//            items(filteredFoodItems) {
//                FoodItemRowCard(item = it, onFoodItemToggle)
//            }
//        }
//    }
}

//@Preview(name = "Light Mode", showSystemUi = true, showBackground = true)
//@Preview(
//    name = "Dark Mode",
//    showSystemUi = true,
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES // Включает темную тему
//)
//@Composable
//fun HomeScreenPreview() {
//    UFMControllerTheme {
//        HomeScreen(
//            filteredFoodItems = listOf(
//                FoodItem(1, "Американский бургер", imageUri = "https://static.vecteezy.com/system/resources/previews/041/290/624/non_2x/ai-generated-fresh-burger-isolated-on-transparent-background-free-png.png", price = 99.99F, inStock = true),
//                FoodItem(2, "Итальянская пицца", imageUri = null, price = 99.99F, inStock = false),
//                FoodItem(2, "Китайский вок", imageUri = null, price = 99.99F, inStock = false),
//                FoodItem(1, "Японские роллы", imageUri = null, price = 99.99F, inStock = true)),
//            onFoodItemToggle = {},
//            searchState = TextFieldState(""),
//            onToggleDrawer={}
//        )
//    }
//}