package com.kalashnikovprojects.ufmtv.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kalashnikovprojects.ufmtv.presentation.ui.screen.LoginScreen
import com.kalashnikovprojects.ufmtv.presentation.ui.screen.MainMenuScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MainMenuRoute) {
        composable<LoginRoute> {
            LoginScreen(
                onNavigateMainScreen = {
                    navController.navigate(MainMenuRoute)
                }
            )
        }
        composable<MainMenuRoute> {
            MainMenuScreen(
                onNavigateLoginScreen = {
                    navController.navigate(LoginRoute)
                }
            )
        }
    }
}