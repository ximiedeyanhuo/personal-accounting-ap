package com.example.personalaccounting.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.personalaccounting.ui.screens.addedit.AddEditTransactionScreen
import com.example.personalaccounting.ui.screens.category.CategoryManagementScreen
import com.example.personalaccounting.ui.screens.home.HomeScreen
import com.example.personalaccounting.ui.screens.statistics.StatisticsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAddTransaction = {
                    navController.navigate(Screen.AddTransaction.route)
                },
                onEditTransaction = { transactionId ->
                    navController.navigate(Screen.EditTransaction.createRoute(transactionId))
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                },
                onNavigateToCategories = {
                    navController.navigate(Screen.CategoryManagement.route)
                }
            )
        }

        composable(Screen.AddTransaction.route) {
            AddEditTransactionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditTransaction.route,
            arguments = listOf(
                navArgument("transactionId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: 0L
            AddEditTransactionScreen(
                transactionId = transactionId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CategoryManagement.route) {
            CategoryManagementScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
