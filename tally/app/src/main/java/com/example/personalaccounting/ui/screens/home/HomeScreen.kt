package com.example.personalaccounting.ui.screens.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    onAddTransaction: () -> Unit,
    onEditTransaction: (Long) -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    Text(text = "Home")
}
