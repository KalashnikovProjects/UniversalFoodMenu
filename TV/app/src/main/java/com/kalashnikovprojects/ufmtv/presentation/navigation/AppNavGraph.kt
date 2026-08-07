package com.kalashnikovprojects.ufmtv.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kalashnikovprojects.ufmtv.presentation.ui.screen.LoadingScreen
import com.kalashnikovprojects.ufmtv.presentation.ui.screen.LoginScreen
import com.kalashnikovprojects.ufmtv.presentation.ui.screen.MainMenuScreen
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.MainViewModel

@Composable
fun AppNavGraph(mainViewModel: MainViewModel, navController: NavHostController) {
    LaunchedEffect(Unit) {
        mainViewModel.navigateLoginScreenEvent.collect {
            Log.d("UFM", "navigate logout")
            mainViewModel.stopListeningUpdates()
            navController.navigate(LoginRoute) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(Unit) {
        mainViewModel.navigateMenuScreenEvent.collect {
            Log.d("UFM", "navigate menu")
            navController.navigate(MenuRoute) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = LoadingRoute) {
        composable<LoadingRoute> {
            LoadingScreen()
        }
        composable<LoginRoute> {
            LoginScreen(
                onSuccess = {
                    navController.navigate(LoadingRoute) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    mainViewModel.listenUpdates()
                }
            )
        }
        composable<MenuRoute> {
            MainMenuScreen()
        }
    }
}