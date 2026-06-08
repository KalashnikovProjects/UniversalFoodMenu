package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemDisplayTypeStyle
import com.example.ufmcontroller.domain.entity.ImageItem
import com.example.ufmcontroller.domain.entity.NotInStockStyle
import com.example.ufmcontroller.domain.entity.ScreenStyle
import com.example.ufmcontroller.domain.entity.ScreenTheme
import com.example.ufmcontroller.domain.entity.Style
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.entity.TextItem
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.AddItemTabContent
import com.example.ufmcontroller.presentation.ui.component.DefaultAppTop
import com.example.ufmcontroller.presentation.ui.component.ScreenTabContent
import com.example.ufmcontroller.presentation.ui.component.SelectedItemTabContent
import com.example.ufmcontroller.presentation.ui.component.TvScreenCard
import com.example.ufmcontroller.presentation.viewmodel.AddDesignExtended
import com.example.ufmcontroller.presentation.viewmodel.InputStates
import com.example.ufmcontroller.presentation.viewmodel.OpenedDeleteConfirmationDialog
import com.example.ufmcontroller.presentation.viewmodel.TvScreenTab
import com.example.ufmcontroller.presentation.viewmodel.TvScreenUiState
import com.example.ufmcontroller.presentation.viewmodel.TvScreenViewModel


@Composable
fun TvScreenScreen(
    screenId: Int,
    viewModel: TvScreenViewModel = hiltViewModel(
        creationCallback = { factory: TvScreenViewModel.Factory ->
            factory.create(screenId)
        }
    ),
    navigateEditFoodItem: (Int) -> Unit,
    navigateEditCategory: (Int) -> Unit,
    onNavigateVisualConfigurationScreen: () -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TvScreenScreenContent(
        uiState,
        viewModel.inputStates,
        viewModel::setUploadedImageUri,
        viewModel::setOpenedDeleteConfirmationDialog,
        viewModel::selectTab,
        viewModel::selectAddDesignExtended,
        viewModel::selectElement,
        viewModel::editCurrentScreen,
        viewModel::deleteScreen,
        viewModel::moveElement,
        viewModel::editCurrentDesignItem,
        viewModel::deleteCurrentDesignItem,
        addImageDesignItem=viewModel::addImageDesignItem,
        addTextDesignItem=viewModel::addTextDesignItem,
        viewModel::addFoodItemDesignItem,
        viewModel::addCategoryDesignItem,
        navigateEditFoodItem,
        navigateEditCategory,
        onNavigateVisualConfigurationScreen,
        onBack,
    )
}

@Composable
fun TvScreenScreenContent(
    uiState: TvScreenUiState,
    inputStates: InputStates,
    setUploadedImageUri: (String) -> Unit,
    setOpenedDeleteConfirmationDialog: (OpenedDeleteConfirmationDialog) -> Unit,
    selectTab: (TvScreenTab) -> Unit,
    selectAddDesignExtended: (AddDesignExtended) -> Unit,
    selectElement: (Int?) -> Unit,
    editCurrentScreen: () -> Unit,
    deleteScreen: () -> Unit,
    onItemMoved: ((id: Int, newX: Float, newY: Float) -> Unit) = { _, _, _ -> },
    editCurrentDesignItem: () -> Unit,
    deleteCurrentDesignItem: () -> Unit,
    addImageDesignItem: () -> Unit,
    addTextDesignItem: () -> Unit,
    addFoodItemDesignItem: (Int) -> Unit,
    addCategoryDesignItem: (Int) -> Unit,
    navigateEditFoodItem: (Int) -> Unit,
    navigateEditCategory: (Int) -> Unit,
    onNavigateVisualConfigurationScreen: () -> Unit,
    onBack: () -> Unit,
) {
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            setUploadedImageUri(uri.toString())
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.openedDeleteConfirmationDialog != OpenedDeleteConfirmationDialog.NONE) {
            AlertDialog(
                title = {
                    Text(
                        text = if (uiState.openedDeleteConfirmationDialog == OpenedDeleteConfirmationDialog.DELETE_DESIGN_ITEM)
                            "Удалить элемент дизайна?" else "Отвязать экран и удалить его настройки отображения?",
                        textAlign = TextAlign.Center
                    )
                },
                onDismissRequest = { setOpenedDeleteConfirmationDialog(OpenedDeleteConfirmationDialog.NONE) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (uiState.openedDeleteConfirmationDialog == OpenedDeleteConfirmationDialog.DELETE_DESIGN_ITEM) {
                                deleteCurrentDesignItem()
                                selectElement(null)
                                setOpenedDeleteConfirmationDialog(OpenedDeleteConfirmationDialog.NONE)
                            } else {
                                deleteScreen()
                                setOpenedDeleteConfirmationDialog(OpenedDeleteConfirmationDialog.NONE)
                                onNavigateVisualConfigurationScreen()
                            }
                        }
                    ) { Text("Да") }
                },
                dismissButton = {
                    TextButton(onClick = { setOpenedDeleteConfirmationDialog(OpenedDeleteConfirmationDialog.NONE) }) {
                        Text("Отмена")
                    }
                }
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            DefaultAppTop(
                text = "Редактирование экрана",
                actions = {
                    IconButton(
                        onClick = { setOpenedDeleteConfirmationDialog(OpenedDeleteConfirmationDialog.DELETE_SCREEN) },
                        colors = IconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = colorScheme.error,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = colorScheme.error.copy(alpha = 0.5F),
                        ),
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                },
                onButton = onBack,
                buttonIsToMenu = false,
            )

            TvScreenCard(
                uiState.screen,
                clickable = false,
                modifier = Modifier.padding(5.dp),
                interactive = true,
                onSelectItem = {
                    selectElement(it)
                },
                selected = uiState.selectedId,
                onItemMoved = onItemMoved,
            )

            SecondaryTabRow(
                selectedTabIndex = uiState.tabsList.indexOf(uiState.tab).coerceAtLeast(0),
                containerColor = colorScheme.surfaceContainerHigh,
                contentColor = colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(100))
            ) {
                uiState.tabsList.forEach { tab ->
                    key("tv_screen_tab_row${tab.ordinal}") {
                        Tab(
                            selected = (uiState.tab == tab),
                            onClick = { selectTab(tab) },
                            text = {
                                val tabText = when (tab) {
                                    TvScreenTab.SELECTED_ITEM -> "Элемент"
                                    TvScreenTab.ADD_ITEM -> "Добавить"
                                    TvScreenTab.SCREEN -> "Экран"
                                }
                                Text(
                                    text = tabText,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 18.sp,
                                )
                            }
                        )
                    }
                }
            }

            Crossfade(
                targetState = uiState.tab,
                label = "TvScreenTabTransition"
            ) { tab ->
                when (tab) {
                    TvScreenTab.SELECTED_ITEM -> SelectedItemTabContent(uiState, inputStates, setOpenedDeleteConfirmationDialog, editCurrentDesignItem)
                    TvScreenTab.ADD_ITEM -> AddItemTabContent(
                        uiState = uiState,
                        inputStates = inputStates,
                        onAddFoodItem = addFoodItemDesignItem,
                        onAddCategory = addCategoryDesignItem,
                        onAddText = addTextDesignItem,
                        onAddImage = addImageDesignItem,
                        onHeaderClick = selectAddDesignExtended,
                        onGalleryClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                        navigateEditFoodItem = navigateEditFoodItem,
                        navigateEditCategory = navigateEditCategory
                    )
                    TvScreenTab.SCREEN -> ScreenTabContent(inputStates, editCurrentScreen)
                }
            }
        }
    }
}

@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TvScreenScreenContentPreview() {
    val tab = remember { mutableStateOf<TvScreenTab>(TvScreenTab.SCREEN) }
    val extended = remember { mutableStateOf<AddDesignExtended>(AddDesignExtended.NONE) }

    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            TvScreenScreenContent(
                uiState = TvScreenUiState(
                    screen = TVScreenWithDesignItems(
                        TVScreen(
                            1,
                            "Экран Redmi Super Max Ultra Pro 42",
                            1920,
                            1080,
                            ScreenStyle(
                                screenTheme = ScreenTheme.BLACK,
                            ),
                        ),
                        designItems = listOf(
                            DesignItem(
                                1,
                                TextItem(
                                    1,
                                    "Бургерная",
                                ),
                                Style(
                                    x = 0.1F,
                                    y = 0.1F,
                                    scale = 3F,
                                ),
                            ),
                            DesignItem(
                                2,
                                FoodItem(
                                    1,
                                    "Одинокий бургер",
                                    105.9F,
                                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                                    false,
                                ),
                                Style(
                                    x = 0.9F,
                                    y = 0.1F,
                                    scale = 1.1F,
                                ),
                            ),
                            DesignItem(
                                3,
                                FoodItem(
                                    1,
                                    "Самый крутой бургер",
                                    105.9F,
                                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                                    true,
                                ),
                                Style(
                                    x = 0.8F,
                                    y = 0.8F,
                                    scale = 2F,
                                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                                ),
                            ),
                            DesignItem(
                                4,
                                CategoryWithFoodItems(
                                    category = Category(
                                        id = 1,
                                        name = "Вкусняшки",
                                        imageUri = "https://i.pinimg.com/originals/b3/24/cb/b324cbabe17d8913e556722973aa6f09.png",
                                        price = 1000.2F,
                                        inStock = null,
                                    ),
                                    foodItems = listOf(
                                        FoodItem(
                                            1,
                                            "Бургер",
                                            105.9F,
                                            "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                                            false,
                                        ),
                                        FoodItem(
                                            1,
                                            "Пицца",
                                            105.9F,
                                            "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                                            true,
                                        )
                                    ),
                                ),
                                Style(
                                    x = 0.1F,
                                    y = 0.45F,
                                    scale = 1.5F,
                                    notInStockStyle = NotInStockStyle.CROSSED_OUT,
                                    showPrice = true,
                                    categoryItemStyle = Style(
                                        notInStockStyle = NotInStockStyle.CROSSED_OUT
                                    )
                                ),
                            ),
                            DesignItem(
                                5,
                                ImageItem(
                                    1,
                                    "https://yt3.googleusercontent.com/ytc/AIdro_kRmcneiyQOhH6n0KVn7FgbCkAfh0AI0EA9RpgzBnHq3g=s900-c-k-c0x00ffffff-no-rj"
                                ),
                                Style(
                                    x = 0.4F,
                                    y = 0.85F,
                                    scale = 1.7F,
                                ),
                            ),
                        )
                    ),
                    selectedId = 1,
                    selected = DesignItem(
                        1,
                        TextItem(
                            1,
                            "Бургерная",
                        ),
                        Style(
                            x = 0.1F,
                            y = 0.1F,
                            scale = 3F,
                        ),
                    ),
                    tab = tab.value,
                    addDesignExtended = extended.value,
                    openedDeleteConfirmationDialog = OpenedDeleteConfirmationDialog.NONE,
                ),
                setOpenedDeleteConfirmationDialog = { },
                selectTab = {
                    tab.value = it
                },
                selectAddDesignExtended = {
                    extended.value = it
                },
                selectElement = { },
                editCurrentScreen = { },
                deleteScreen = { },
                editCurrentDesignItem = { },
                deleteCurrentDesignItem = { },
                onBack = { },
                addImageDesignItem = { },
                addTextDesignItem = { },
                addFoodItemDesignItem = { },
                addCategoryDesignItem = { },
                inputStates = InputStates(),
                navigateEditFoodItem = { },
                navigateEditCategory = { },
                onNavigateVisualConfigurationScreen = { },
                setUploadedImageUri = { },
            )
        }
    }
}