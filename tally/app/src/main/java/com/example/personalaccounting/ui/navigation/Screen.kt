package com.example.personalaccounting.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddTransaction : Screen("add_transaction")
    object EditTransaction : Screen("edit_transaction/{transactionId}") {
        fun createRoute(transactionId: Long) = "edit_transaction/$transactionId"
    }
    object CategoryManagement : Screen("category_management")
    object Statistics : Screen("statistics")
    object Settings : Screen("settings")
}
