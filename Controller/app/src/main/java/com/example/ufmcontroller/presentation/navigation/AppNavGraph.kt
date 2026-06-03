package com.example.ufmcontroller.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ufmcontroller.presentation.ui.screen.AboutAppScreen
import com.example.ufmcontroller.presentation.ui.screen.AddTvScreenScreen
import com.example.ufmcontroller.presentation.ui.screen.EditMenuScreen
import com.example.ufmcontroller.presentation.ui.screen.HomeScreen
import com.example.ufmcontroller.presentation.viewmodel.MainViewModel
import com.example.ufmcontroller.presentation.ui.screen.LoginScreen
import com.example.ufmcontroller.presentation.ui.screen.SettingsScreen
import com.example.ufmcontroller.presentation.ui.screen.VisualConfigurationScreen

@Composable
fun AppNavGraph(mainViewModel: MainViewModel,navController: NavHostController, onToggleDrawer: () -> Unit) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                navigateEditCategory={
                    id ->
                    navController.navigate(EditCategoryRoute(id))
                },
                navigateEditFoodItem={
                    id ->
                    navController.navigate(EditItemRoute(id))
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<MenuEditRoute> {
            EditMenuScreen(
                onNavigateToEditFoodItem = {
                        id ->
                    navController.navigate(EditItemRoute(id))
                },
                onNavigateToEditCategory = {
                        id ->
                    navController.navigate(EditCategoryRoute(id))
                },
                onNavigateToAddFoodItem = {
                    navController.navigate(AddItemRoute)
                },
                onNavigateToAddCategoryItem = {
                    navController.navigate(AddCategoryRoute)
                },
                onToggleDrawer = onToggleDrawer,

            )
        }
        composable<VisualConfigurationRoute> {
            VisualConfigurationScreen (
                onNavigateToScreen = {
                    id ->
                    navController.navigate(TvScreenRoute(id))
                },
                onAddScreen = {
                    navController.navigate(AddTvScreenRoute)
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<AddTvScreenRoute> {
            AddTvScreenScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable<SettingsRoute> {
            SettingsScreen(
                onLogout = {
                    mainViewModel.stopEventsService()

                    navController.navigate(LoginRoute)
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<AboutAppRoute> {
            AboutAppScreen(
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<LoginRoute> {
            LoginScreen(
                onSuccessfulLogin = {
                    mainViewModel.startEventsService()

                    navController.navigate(HomeRoute)
                }
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