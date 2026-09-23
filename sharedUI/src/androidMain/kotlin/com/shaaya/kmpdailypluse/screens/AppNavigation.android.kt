package com.shaaya.kmpdailypluse.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    data object Articles : Screen("articles")
    data object About : Screen("about")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Articles.route
    ) {
        composable(Screen.Articles.route) {
            ArticleScreen(
                onAboutClick = {
                    navController.navigate(Screen.About.route)
                }
            )
        }
        composable(Screen.About.route) {
            AboutScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
