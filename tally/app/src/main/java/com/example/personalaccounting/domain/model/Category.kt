package com.example.personalaccounting.domain.model

import java.time.LocalDateTime

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String = "",
    val color: String = "",
    val type: TransactionType,
    val isDefault: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
