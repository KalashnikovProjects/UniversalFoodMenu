package com.example.ufmcontroller.data.local

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.DesignItemWithScreenId
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.entity.TextItem
import com.example.ufmcontroller.domain.entity.defaultTVScreen
import com.example.ufmcontroller.domain.entity.toDesignItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Int
import kotlin.Pair
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.mapNotNull
import kotlin.text.category

@Singleton
class LocalDataSource @Inject constructor(
) {
    private val _designItemsRaw = MutableStateFlow<Map<Int, DesignItemWithScreenId>>(emptyMap())
    private val _categoriesRaw = MutableStateFlow<Map<Int, Category>>(emptyMap())
    private val _foodItemsRaw = MutableStateFlow<Map<Int, FoodItem>>(emptyMap())
    private val _relationsRaw = MutableStateFlow<List<Pair<Int, Int>>>(emptyList())
    private val _screensRaw = MutableStateFlow<Map<Int, TVScreen>>(emptyMap())


    val foodItems: Flow<List<FoodItem>> = _foodItemsRaw.map { it.map { it.value } }
    val categories: Flow<List<Category>> = _categoriesRaw.map { it.map { it.value } }
    val screens: Flow<List<TVScreen>> = _screensRaw.map { it.map { it.value } }

    val categoriesWithFoodItems: Flow<List<CategoryWithFoodItems>> = combine(
        _categoriesRaw, _foodItemsRaw, _relationsRaw
    ) { categories, foods, relations ->
        categories.map { (id, category) ->
            CategoryWithFoodItems(
                category,
                relations.filter { it.first == id }.mapNotNull { foods[it.second] }
            )
        }
    }

    val noCategoryFoodItems: Flow<List<FoodItem>> = foodItems.combine(
        _relationsRaw
    ) { foods, relations ->
        foods.filter { food ->
            relations.none { it.second == food.id }
        }
    }

    val categorizedFoodItems: Flow<FoodItemsCategorized> = categoriesWithFoodItems.combine(
        noCategoryFoodItems
    ) {
        c, noC ->
        FoodItemsCategorized(c, noC)
    }

    val designItemsWithScreenId: Flow<List<DesignItemWithScreenId>> = combine(
        _designItemsRaw, categoriesWithFoodItems, _foodItemsRaw
    ) { raws, categoriesWithFoodItems, foods ->
        raws.map { designItem ->
            when (val element = designItem.value.element) {
                is CategoryWithFoodItems -> {
                    designItem.value.copy(
                        element = categoriesWithFoodItems.find { it.category.id == element.category.id } ?: element
                    )
                }
                is FoodItem -> {
                    designItem.value.copy(element = foods[element.id] ?: element)
                }
                else -> designItem.value
            }
        }
    }
    val screensWithDesignItems: Flow<List<TVScreenWithDesignItems>> = screens.combine(
        designItemsWithScreenId
    ) {
        s, d ->
        s.map {
            screen ->
            TVScreenWithDesignItems(
                screen,
                d.filter { it.screenId == screen.id }.map { it.toDesignItem() }
            )
        }

    }
    val designItems: Flow<List<DesignItem>> = designItemsWithScreenId.map { it.map { it.toDesignItem() } }


    fun getFoodItem(id: Int): Flow<FoodItem> {
        return foodItems.map { f -> f.find { it.id == id }!! }
    }

    fun getCategory(id: Int): Flow<Category> {
        return categories.map { f -> f.find { it.id == id }!! }
    }

    fun getDesignItem(id: Int): Flow<DesignItem> {
        return designItems.map { f -> f.find { it.id == id }!! }
    }

    fun getScreenWithDesignItems(id: Int): Flow<TVScreenWithDesignItems> {
        return screensWithDesignItems.map { f -> f.find { it.tvScreen.id == id }
            ?: TVScreenWithDesignItems(defaultTVScreen(), emptyList()) }
    }

    fun getCategoryByFoodId(foodId: Int): Flow<List<Category>> {
        return categories.combine(
            _relationsRaw
        ) { categories, relations ->
            categories.filter { category ->
                relations.contains(category.id to foodId)
            }
        }
    }

    fun getFoodItemsByCategoryId(categoryId: Int): Flow<List<FoodItem>> {
        return foodItems.combine(
            _relationsRaw
        ) { foodItems, relations ->
            foodItems.filter { foodItem ->
                relations.contains(categoryId to foodItem.id)
            }
        }
    }

    fun updateFoodItem(id: Int, foodItem: FoodItem) {
        _foodItemsRaw.update { it + (id to foodItem) }
    }

    fun toggleFoodInStock(id: Int, inStock: Boolean) {
        _foodItemsRaw.update { map ->
            map.mapValues { if (it.value.id == id) it.value.copy(inStock = inStock) else it.value }
        }
    }

    fun deleteFoodItem(id: Int) {
        _foodItemsRaw.update { it - id }
        _relationsRaw.update {
            it.filter { pair -> pair.second != id }
        }
    }

    fun updateCategory(id: Int, category: Category) {
        _categoriesRaw.update { it + (id to category) }
    }

    fun toggleCategoryInStock(id: Int, inStock: Boolean) {
        _categoriesRaw.update { map ->
            map.mapValues { if (it.value.id == id) it.value.copy(inStock = inStock) else it.value }
        }
    }

    fun deleteCategory(id: Int) {
        _categoriesRaw.update { it - id }
        _relationsRaw.update {
            it.filter { pair -> pair.first != id }
        }
    }

    fun setFoodCategories(foodId: Int, categoryIds: List<Int>) {
        _relationsRaw.update { list ->
            list.filter { it.second != foodId } + categoryIds.map { catId -> Pair(catId, foodId) }
        }
    }

    fun setCategoryItems(categoryId: Int, foodIds: List<Int>) {
        _relationsRaw.update { list ->
            list.filter { it.first != categoryId } + foodIds.map { foodId -> Pair(categoryId, foodId) }
        }
    }

    fun updateDesignItem(id: Int, designItem: DesignItemWithScreenId) {
        _designItemsRaw.update { it + (id to designItem) }
    }

    fun deleteDesignItem(id: Int) {
        _designItemsRaw.update { it - id }
    }

    fun updateTextItemInDesign(id: Int, textItem: TextItem) {
        _designItemsRaw.update { map ->
            map.mapValues { entry ->
                val element = entry.value.element
                if (element is TextItem && element.id == id) {
                    entry.value.copy(element = textItem)
                } else {
                    entry.value
                }
            }
        }
    }

    fun updateScreen(id: Int, screen: TVScreen) {
        _screensRaw.update { it + (id to screen) }
    }

    fun deleteScreen(id: Int) {
        _screensRaw.update { it - id }
    }

    fun reloadScreens(screens: Map<Int, TVScreen>) {
        _screensRaw.value = screens
    }

    fun reloadDesignItems(items: Map<Int, DesignItemWithScreenId>) {
        _designItemsRaw.value = items
    }

    fun reloadAllCategorizedItems(
        categories: Map<Int, Category>,
        foodItems: Map<Int, FoodItem>,
        relations: List<Pair<Int, Int>>
    ) {
        _categoriesRaw.value = categories
        _foodItemsRaw.value = foodItems
        _relationsRaw.value = relations
    }
}