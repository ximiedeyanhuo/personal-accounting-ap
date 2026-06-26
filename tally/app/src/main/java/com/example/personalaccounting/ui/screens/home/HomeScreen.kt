package com.example.personalaccounting.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalaccounting.ui.components.TransactionItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.TreeMap
import androidx.compose.foundation.ExperimentalFoundationApi

private sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Statistics : BottomNavItem("statistics", "Statistics", Icons.Default.List)
    object Settings : BottomNavItem("settings", "Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onAddTransaction: () -> Unit,
    onEditTransaction: (Long) -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToSettings: () -> Unit,
    currentRoute: String = "home",
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    val monthlySummary by viewModel.monthlySummary.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personal Accounting") },
                actions = {
                    IconButton(onClick = onNavigateToCategories) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Categories"
                        )
                    }
                }
            )
        },
        bottomBar = {
            val items = listOf(BottomNavItem.Home, BottomNavItem.Statistics, BottomNavItem.Settings)
            BottomNavigation {
                items.forEach { item ->
                    BottomNavigationItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            when (item) {
                                BottomNavItem.Home -> { }
                                BottomNavItem.Statistics -> onNavigateToStatistics()
                                BottomNavItem.Settings -> onNavigateToSettings()
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransaction) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Transaction"
                )
            }
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
            ) {
                monthlySummary?.let { summary ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
                
                val groupedTransactions = transactions
                    .groupBy { it.date }
                    .toSortedMap(compareByDescending { it })
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    groupedTransactions.forEach { (date, dateTransactions) ->
                        stickyHeader {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colors.surface
                            ) {
                                Text(
                                    text = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.subtitle2,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                        items(dateTransactions) { transaction ->
                            val category = viewModel.getCategoryById(transaction.categoryId)
                            TransactionItem(
                                transaction = transaction,
                                category = category,
                                onTransactionClick = onEditTransaction
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}
