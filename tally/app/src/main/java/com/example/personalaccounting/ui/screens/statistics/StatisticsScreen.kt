package com.example.personalaccounting.ui.screens.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalaccounting.domain.model.CategoryExpense
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StatisticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: StatisticsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val categoryExpenses by viewModel.categoryExpenses.collectAsState()
    val monthlySummary by viewModel.monthlySummary.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.previousMonth() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Previous Month"
                        )
                    }

                    Text(
                        text = "${LocalDate.of(selectedYear, selectedMonth, 1).month.getDisplayName(TextStyle.FULL, Locale.getDefault())} $selectedYear",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { viewModel.nextMonth() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Next Month",
                            modifier = Modifier.graphicsLayer(rotationZ = 180f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                monthlySummary?.let { summary ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Monthly Summary",
                                style = MaterialTheme.typography.h6,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Income",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$%.2f".format(summary.totalIncome),
                                        style = MaterialTheme.typography.h6,
                                        color = Color(0xFF4CAF50)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Expense",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$%.2f".format(summary.totalExpense),
                                        style = MaterialTheme.typography.h6,
                                        color = Color(0xFFF44336)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Balance",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$%.2f".format(summary.balance),
                                        style = MaterialTheme.typography.h6,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Expenses by Category",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (categoryExpenses.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No expenses for this month",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(categoryExpenses) { categoryExpense ->
                            CategoryExpenseItem(categoryExpense = categoryExpense)
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryExpenseItem(categoryExpense: CategoryExpense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = categoryExpense.categoryName,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${categoryExpense.transactionCount} transactions",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$%.2f".format(categoryExpense.totalAmount),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "%.1f%%".format(categoryExpense.percentage),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
