package com.kalashnikovprojects.ufmtv.data.local

import com.kalashnikovprojects.ufmtv.domain.entity.Category
import com.kalashnikovprojects.ufmtv.domain.entity.CategoryWithFoodItems
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.Designable
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.ImageItem
import com.kalashnikovprojects.ufmtv.domain.entity.TVScreen
import com.kalashnikovprojects.ufmtv.domain.entity.TextItem
import com.kalashnikovprojects.ufmtv.domain.entity.defaultTVScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

// TODO: maybe dont work
@Singleton
class MainDataSource @Inject constructor(
    private val externalScope: CoroutineScope
) {
    private val _designItemsRaw = MutableStateFlow<List<DesignItem>>(emptyList())
    private val _categories = MutableStateFlow<Map<Int, Category>>(emptyMap())
    private val _foodItems = MutableStateFlow<Map<Int, FoodItem>>(emptyMap())

    private val _categoryFoodRelations = MutableStateFlow<Map<Int, List<Int>>>(emptyMap())

    private val _currentScreen = MutableStateFlow(defaultTVScreen())
    val currentScreen: StateFlow<TVScreen> = _currentScreen.asStateFlow()

    val foodItems: StateFlow<List<FoodItem>> = _foodItems
        .combine(_foodItems) { map, _ -> map.values.toList() }
        .stateIn(externalScope, SharingStarted.Eagerly, emptyList())

    val designItems: StateFlow<List<DesignItem>> = combine(
        _designItemsRaw, _categories, _foodItems, _categoryFoodRelations
    ) { raws, categories, foods, relations ->
        raws.map { designItem ->
            when (val element = designItem.element) {
                is CategoryWithFoodItems -> {
                    val currentCategory = categories[element.category.id] ?: element.category
                    val foodIds = relations[element.category.id] ?: emptyList()
                    val mappedFoods = foodIds.mapNotNull { foods[it] }

                    designItem.copy(
                        element = element.copy(
                            category = currentCategory,
                            foodItems = mappedFoods
                        )
                    )
                }
                is FoodItem -> {
                    designItem.copy(element = foods[element.id] ?: element)
                }
                else -> designItem
            }
        }
    }.stateIn(externalScope, SharingStarted.Eagerly, emptyList())

    fun updateDesignItemsRaw(items: List<DesignItem>) {
        _designItemsRaw.value = items

        val newCategories = mutableMapOf<Int, Category>()
        val newRelations = mutableMapOf<Int, List<Int>>()
        val newFoods = _foodItems.value.toMutableMap()

        items.forEach { item ->
            when (val element = item.element) {
                is CategoryWithFoodItems -> {
                    newCategories[element.category.id] = element.category
                    newRelations[element.category.id] = element.foodItems.map { it.id }
                    element.foodItems.forEach { newFoods[it.id] = it }
                }
                is FoodItem -> {
                    newFoods[element.id] = element
                }
                else -> element
            }
        }
        _categories.value = _categories.value + newCategories
        _categoryFoodRelations.value = _categoryFoodRelations.value + newRelations
        _foodItems.value = newFoods
    }

    fun updateCurrentScreen(screen: TVScreen) {
        _currentScreen.value = screen
    }

    fun putFood(food: FoodItem) {
        _foodItems.value = _foodItems.value + (food.id to food)
    }

    fun deleteFood(foodId: Int) {
        _foodItems.value = _foodItems.value - foodId
        _categoryFoodRelations.value = _categoryFoodRelations.value.mapValues { (_, list) ->
            list.filter { it != foodId }
        }
    }

    fun putCategory(category: Category) {
        _categories.value = _categories.value + (category.id to category)
    }

    fun deleteCategory(categoryId: Int) {
        _categories.value = _categories.value - categoryId
        _categoryFoodRelations.value = _categoryFoodRelations.value - categoryId
        _designItemsRaw.value = _designItemsRaw.value.filter {
            val element = it.element
            !(element is CategoryWithFoodItems && element.category.id == categoryId)
        }
    }

    fun setCategoryFoodRelations(categoryId: Int, foodIds: List<Int>) {
        _categoryFoodRelations.value = _categoryFoodRelations.value + (categoryId to foodIds)
    }

    fun updateFoodRelationsForCategories(foodId: Int, categoryIds: List<Int>) {
        val currentRelations = _categoryFoodRelations.value.toMutableMap()

        _categories.value.keys.forEach { catId ->
            val list = currentRelations[catId] ?: emptyList()
            if (categoryIds.contains(catId)) {
                if (!list.contains(foodId)) currentRelations[catId] = list + foodId
            } else {
                currentRelations[catId] = list.filter { it != foodId }
            }
        }
        _categoryFoodRelations.value = currentRelations
    }

    fun putDesignItemRaw(item: DesignItem) {
        _designItemsRaw.value = _designItemsRaw.value.map { if (it.id == item.id) item else it }
    }

    fun addDesignItemRaw(item: DesignItem) {
        _designItemsRaw.value = _designItemsRaw.value + item
        when (val element = item.element) {
            is CategoryWithFoodItems -> {
                putCategory(element.category)
                setCategoryFoodRelations(element.category.id, element.foodItems.map { it.id })
                element.foodItems.forEach { putFood(it) }
            }
            is FoodItem -> putFood(element)
            else -> element
        }
    }

    fun deleteDesignItemRaw(id: Int) {
        _designItemsRaw.value = _designItemsRaw.value.filter { it.id != id }
    }

    fun updateDesignItemElement(id: Int, transform: (Any) -> Any) {
        _designItemsRaw.value = _designItemsRaw.value.map { item ->
            if (item.id == id) item.copy(element = transform(item.element) as Designable) else item
        }
    }
}