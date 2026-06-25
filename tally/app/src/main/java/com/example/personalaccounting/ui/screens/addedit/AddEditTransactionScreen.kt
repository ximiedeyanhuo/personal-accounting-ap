package com.example.personalaccounting.ui.screens.addedit

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun AddEditTransactionScreen(
    transactionId: Long? = null,
    onNavigateBack: () -> Unit
) {
    Text(text = "Add/Edit Transaction")
}
