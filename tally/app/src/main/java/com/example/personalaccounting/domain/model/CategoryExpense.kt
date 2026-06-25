package com.example.personalaccounting.domain.model

data class CategoryExpense(
    val categoryId: Long,
    val categoryName: String,
    val totalAmount: Double,
    val transactionCount: Int,
    val percentage: Double = 0.0
)
