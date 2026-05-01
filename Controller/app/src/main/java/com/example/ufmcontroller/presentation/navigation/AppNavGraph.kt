package com.example.ufmcontroller.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.ufmcontroller.presentation.ui.screen.AboutAppScreen
import com.example.ufmcontroller.presentation.ui.screen.HomeScreen
import com.example.ufmcontroller.presentation.viewmodel.MainViewModel

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: MainViewModel, onToggleDrawer: () -> Unit) {
    val filteredFoodItems by viewModel.filteredFoodItems.collectAsStateWithLifecycle()
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                filteredFoodItems = filteredFoodItems,
                onFoodItemToggle = viewModel::toggleFoodItem,
                searchState = viewModel.searchState,
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<AboutAppRoute> {
            AboutAppScreen(
                onToggleDrawer=onToggleDrawer,
            )
        }
//        composable<ItemRoute> { backStackEntry ->
//            val route: ItemRoute = backStackEntry.toRoute()
//
//            val todo = foodItems.find { it.id == route.id }
//
//            todo?.let {
//                ItemScreen(
//                    todo = it,
//                    onBack = { navController.popBackStack() },
//                    onToggle = viewModel::toggleTodo,
//                )
//            }
//        }
    }
}