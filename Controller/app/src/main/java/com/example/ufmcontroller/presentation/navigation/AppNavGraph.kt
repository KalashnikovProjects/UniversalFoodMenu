package com.example.ufmcontroller.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.ufmcontroller.presentation.ui.screen.AboutAppScreen
import com.example.ufmcontroller.presentation.ui.screen.AddCategoryScreen
import com.example.ufmcontroller.presentation.ui.screen.AddFoodItemScreen
import com.example.ufmcontroller.presentation.ui.screen.AddTvScreenScreen
import com.example.ufmcontroller.presentation.ui.screen.EditCategoryScreen
import com.example.ufmcontroller.presentation.ui.screen.EditFoodItemScreen
import com.example.ufmcontroller.presentation.ui.screen.EditMenuScreen
import com.example.ufmcontroller.presentation.ui.screen.HomeScreen
import com.example.ufmcontroller.presentation.viewmodel.MainViewModel
import com.example.ufmcontroller.presentation.ui.screen.LoginScreen
import com.example.ufmcontroller.presentation.ui.screen.SettingsScreen
import com.example.ufmcontroller.presentation.ui.screen.TvScreenScreen
import com.example.ufmcontroller.presentation.ui.screen.VisualConfigurationScreen

@Composable
fun AppNavGraph(mainViewModel: MainViewModel, navController: NavHostController, onToggleDrawer: () -> Unit) {
    LaunchedEffect(Unit) {
        Log.d("UFM", "mainViewModel.logoutEvent in AppNavGraph")
        mainViewModel.uiLogoutEvent.collect {
            Log.d("UFM", "navigate logout")
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != LoginRoute::class.qualifiedName) {
                navController.navigate(LoginRoute) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                navigateEditFoodItem={
                    id ->
                    navController.navigate(EditItemRoute(id)) {
                        launchSingleTop = true
                    }
                },
                navigateEditCategory={
                    id ->
                    navController.navigate(EditCategoryRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onEditMenu = {
                    navController.navigate(MenuEditRoute) {
                        launchSingleTop = true
                    }
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<MenuEditRoute> {
            EditMenuScreen(
                onNavigateToEditFoodItem = {
                        id ->
                    navController.navigate(EditItemRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToEditCategory = {
                        id ->
                    navController.navigate(EditCategoryRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAddFoodItem = {
                    navController.navigate(AddItemRoute) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAddCategoryItem = {
                    navController.navigate(AddCategoryRoute) {
                        launchSingleTop = true
                    }
                },
                onToggleDrawer = onToggleDrawer,
            )
        }
        composable<AddItemRoute> {
            AddFoodItemScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable<EditItemRoute> {
                backStackEntry ->
            val route: EditItemRoute = backStackEntry.toRoute()

            EditFoodItemScreen (
                foodId = route.id,
                onBack = { navController.popBackStack() },
            )
        }
        composable<AddCategoryRoute> {
            AddCategoryScreen(
                onBack = { navController.popBackStack() },
                navigateEditFoodItem = {
                        id ->
                    navController.navigate(EditItemRoute(id)) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable<EditCategoryRoute> {
                backStackEntry ->
            val route: EditCategoryRoute = backStackEntry.toRoute()

            EditCategoryScreen(
                categoryId = route.id,
                onBack = { navController.popBackStack() },
                navigateEditFoodItem = {
                        id ->
                    navController.navigate(EditItemRoute(id)) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable<TvScreenRoute> {
                backStackEntry ->
            val route: TvScreenRoute = backStackEntry.toRoute()

            TvScreenScreen (
                screenId = route.id,
                navigateEditFoodItem =  {
                        id ->
                    navController.navigate(EditItemRoute(id)) {
                        launchSingleTop = true
                    }
                },
                navigateEditCategory = {
                        id ->
                    navController.navigate(EditCategoryRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateVisualConfigurationScreen = {
                    navController.navigate(VisualConfigurationRoute) {
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable<VisualConfigurationRoute> {
            VisualConfigurationScreen (
                onNavigateToScreen = {
                    id ->
                    navController.navigate(TvScreenRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onAddScreen = {
                    navController.navigate(AddTvScreenRoute) {
                        launchSingleTop = true
                    }
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<AddTvScreenRoute> {
            AddTvScreenScreen(
                onNavigateAfterLogin = {
                    navController.navigate(TvScreenRoute(it)) {
                        popUpTo(AddTvScreenRoute) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    } },
                onBack = {
                    navController.popBackStack() },
            )
        }
        composable<SettingsRoute> {
            SettingsScreen(
                onLogout = {
                    mainViewModel.stopEventsService()

                    navController.navigate(LoginRoute) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
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

                    navController.navigate(HomeRoute) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}