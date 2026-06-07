package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.DesignItemWithScreenId
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.ImageItem
import com.example.ufmcontroller.domain.entity.Style
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.entity.TextItem
import com.example.ufmcontroller.domain.entity.defaultTVScreen
import com.example.ufmcontroller.domain.entity.toDesignItemWithScreenId
import com.example.ufmcontroller.domain.usecase.category.GetCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.design.AddDesignItemUseCase
import com.example.ufmcontroller.domain.usecase.design.AddDesignItemWithImageUseCase
import com.example.ufmcontroller.domain.usecase.design.AddDesignItemWithTextUseCase
import com.example.ufmcontroller.domain.usecase.design.DeleteDesignItemUseCase
import com.example.ufmcontroller.domain.usecase.design.EditDesignItemUseCase
import com.example.ufmcontroller.domain.usecase.design.GetDesignItemUseCase
import com.example.ufmcontroller.domain.usecase.food.GetFoodItemsUseCase
import com.example.ufmcontroller.domain.usecase.screen.DeleteScreenUseCase
import com.example.ufmcontroller.domain.usecase.screen.EditScreenUseCase
import com.example.ufmcontroller.domain.usecase.screen.GetScreenWithDesignItemsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


enum class TvScreenTab {
    SELECTED_ITEM,
    ADD_ITEM,
    SCREEN,
}

enum class AddDesignExtended {
    NONE,
    FOOD_ITEMS,
    CATEGORIES,
    TEXT,
    IMAGE
}

enum class OpenedDeleteConfirmationDialog {
    NONE,
    DELETE_SCREEN,
    DELETE_DESIGN_ITEM,
}


data class TvScreenUiState (
    val screen: TVScreenWithDesignItems,
    val selectedId: Int?=null,
    val selected: DesignItem?=null,
    val tab: TvScreenTab = TvScreenTab.SCREEN,
    val addDesignExtended: AddDesignExtended = AddDesignExtended.NONE,
    val openedDeleteConfirmationDialog: OpenedDeleteConfirmationDialog = OpenedDeleteConfirmationDialog.NONE,
    val tabsList: List<TvScreenTab> = listOf(
        TvScreenTab.SELECTED_ITEM,
        TvScreenTab.ADD_ITEM,
        TvScreenTab.SCREEN,
    ),
    val foodItems: List<FoodItem> = emptyList(),
    val categories: List<Category> = emptyList(),
    val uploadedImagUri: String? = null,
    )

data class InputStates (
    val scaleSliderValue: MutableStateFlow<Float> = MutableStateFlow(1F),
    val textItemFieldState: TextFieldState = TextFieldState(""),
    val screenNameFieldState: TextFieldState = TextFieldState(""),

    val foodItemsSearchState: TextFieldState = TextFieldState(""),
    val categoriesSearchState: TextFieldState = TextFieldState(""),
)

@HiltViewModel(assistedFactory = TvScreenViewModel.Factory::class)
class TvScreenViewModel @AssistedInject constructor(
    private val getFoodItemsUseCase: GetFoodItemsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,

    private val getScreenWithDesignItemsUseCase: GetScreenWithDesignItemsUseCase,
    private val editScreenUseCase: EditScreenUseCase,
    private val deleteScreenUseCase: DeleteScreenUseCase,

    private val getDesignItemUseCase: GetDesignItemUseCase,
    private val addDesignItemUseCase: AddDesignItemUseCase,
    private val addDesignItemWithImageUseCase: AddDesignItemWithImageUseCase,
    private val addDesignItemWithTextUseCase: AddDesignItemWithTextUseCase,

    private val editDesignItemUseCase: EditDesignItemUseCase,
    private val deleteDesignItemUseCase: DeleteDesignItemUseCase,
    @Assisted private val id: Int
) : ViewModel() {
    private val foodItemsFlow = getFoodItemsUseCase()
    private val categoriesFlow = getCategoriesUseCase()

    val inputStates: InputStates = InputStates()
    private val foodSearchQueryFlow = snapshotFlow { inputStates.categoriesSearchState.text }
    private val categoriesQueryFlow = snapshotFlow { inputStates.categoriesSearchState.text }

    private val filteredFoodItemsFlow = combine(
        foodItemsFlow,
        foodSearchQueryFlow,
    ) { items, query ->
        if (query.isBlank()) {
            items
        } else {
            items.filter {
                it.name.contains(
                    query,
                    ignoreCase = true
                )
            }
        }
    }
    private val filteredCategoriesFlow = combine(
        categoriesFlow,
        categoriesQueryFlow,
    ) { items, query ->
        if (query.isBlank()) {
            items
        } else {
            items.filter {
                it.name.contains(
                    query,
                    ignoreCase = true
                )
            }
        }
    }
    // TODO: init loading

    private val _selectedId = MutableStateFlow<Int?>(null)
    private val _selected = MutableStateFlow<DesignItem?>(null)
    private val _screen = MutableStateFlow<TVScreenWithDesignItems?>(null)
    private val _tab = MutableStateFlow<TvScreenTab>(TvScreenTab.SCREEN)
    private val _addDesignExtended = MutableStateFlow<AddDesignExtended>(AddDesignExtended.NONE)
    private val _openedDeleteConfirmationDialog = MutableStateFlow<OpenedDeleteConfirmationDialog>(OpenedDeleteConfirmationDialog.NONE)
    private val _uploadImageUriFlow = MutableStateFlow<String?>(null)

    val uiState: StateFlow<TvScreenUiState> = combine(
        getScreenWithDesignItemsUseCase(id),
        _selectedId,
        _selected,
        _screen,
        _tab,
        _addDesignExtended,
        _openedDeleteConfirmationDialog,
        filteredFoodItemsFlow,
        filteredCategoriesFlow,
        _uploadImageUriFlow
    ) { flows ->
        TvScreenUiState(
            screen = (flows[3] ?: flows[0]) as TVScreenWithDesignItems,
            selectedId = flows[1] as Int?,
            selected = flows[2] as DesignItem?,
            tab = flows[4] as TvScreenTab,
            addDesignExtended = flows[5] as AddDesignExtended,
            openedDeleteConfirmationDialog = flows[6] as OpenedDeleteConfirmationDialog,
            tabsList = if (flows[2] as DesignItem? != null) listOf(
                TvScreenTab.SELECTED_ITEM,
                TvScreenTab.ADD_ITEM,
                TvScreenTab.SCREEN,
            ) else listOf(
                TvScreenTab.ADD_ITEM,
                TvScreenTab.SCREEN,
            ),
            foodItems = flows[7] as List<FoodItem>,
            categories = flows[8] as List<Category>,
            uploadedImagUri = flows[9] as String?,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TvScreenUiState(
            screen = TVScreenWithDesignItems(
                tvScreen=defaultTVScreen(),
                designItems=emptyList()
            ),
        )
    )

    fun setOpenedDeleteConfirmationDialog(dialog: OpenedDeleteConfirmationDialog) {
        _openedDeleteConfirmationDialog.value = dialog
    }

    fun setUploadedImageUri(uri: String) {
        _uploadImageUriFlow.value = uri
    }

    fun selectTab(tab: TvScreenTab) {
        _tab.value = tab
    }

    fun selectAddDesignExtended(extended: AddDesignExtended) {
        _addDesignExtended.value = extended
    }

    fun selectElement(id: Int?) {
        viewModelScope.launch {
            _selectedId.value = id
            if (id == null) {
                _selected.value = null
                if (_tab.value == TvScreenTab.SELECTED_ITEM) {
                    selectTab(TvScreenTab.SCREEN)
                }
            } else {
                _selected.value = getDesignItemUseCase(id).first()
                selectTab(TvScreenTab.SELECTED_ITEM)
            }
        }
    }

    fun setScreen(screen: TVScreen) {
        _screen.value = _screen.value?.copy(tvScreen = screen)
    }

    fun editScreen() {
        viewModelScope.launch {
            _screen.value?.let {
                editScreenUseCase(id, it.tvScreen)
            }
        }
    }

    fun deleteScreen() {
        viewModelScope.launch {
            deleteScreenUseCase(id)
        }
    }

    fun editCurrentDesignItem() {
        viewModelScope.launch {
            _selected.value?.let {
                editDesignItemUseCase(id, it.toDesignItemWithScreenId(screenId = id))
            }
        }
    }

    fun deleteCurrentDesignItem() {
        viewModelScope.launch {
            _selectedId.value?.let {
                deleteDesignItemUseCase(it)
            }
        }
    }

    fun addImageDesignItem() {
        viewModelScope.launch {
            _uploadImageUriFlow.value?.let {
                val item = ImageItem(
                    imageUri = it
                )
                addDesignItemWithImageUseCase(
                    DesignItemWithScreenId(
                        screenId = id,
                        element = item,
                        style = Style(),
                    )
                )
                _uploadImageUriFlow.value = null
            }
        }
    }

    fun addTextDesignItem() {
        viewModelScope.launch {
            val item = TextItem(
                text = inputStates.textItemFieldState.text.toString()
            )
            addDesignItemWithTextUseCase(DesignItemWithScreenId(
                screenId = id,
                element = item,
                style = Style(),
            ))
            inputStates.textItemFieldState.clearText()
        }
    }

    fun addFoodItemDesignItem(foodId: Int) {
        viewModelScope.launch {
            val item = FoodItem(
                id=foodId,
                name="",
                price=0F,
                imageUri="",
                inStock=false
            )
            addDesignItemUseCase(DesignItemWithScreenId(
                screenId = id,
                element = item,
                style = Style(),
            ))
        }
    }

    fun addCategoryDesignItem(categoryId: Int) {
        viewModelScope.launch {
            val item = Category(
                id=categoryId,
                name="",
            )
            addDesignItemUseCase(DesignItemWithScreenId(
                screenId = id,
                element = item,
                style = Style(),
            ))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(id: Int): TvScreenViewModel
    }
}