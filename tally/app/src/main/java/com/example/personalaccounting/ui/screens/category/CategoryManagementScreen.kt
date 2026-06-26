package com.example.personalaccounting.ui.screens.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType

@Composable
fun CategoryManagementScreen(
    onNavigateBack: () -> Unit,
    viewModel: CategoryManagementViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val name by viewModel.name.collectAsState()
    val type by viewModel.type.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    var showDeleteDialog by remember { mutableStateOf<Category?>(null) }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            viewModel.resetSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Category Management") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Add New Category",
                            style = MaterialTheme.typography.h6
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { viewModel.updateName(it) },
                            label = { Text("Category Name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = errorMessage != null
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Type",
                            style = MaterialTheme.typography.subtitle1
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = type == TransactionType.EXPENSE,
                                onClick = { viewModel.updateType(TransactionType.EXPENSE) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Expense")
                            }

                            FilterChip(
                                selected = type == TransactionType.INCOME,
                                onClick = { viewModel.updateType(TransactionType.INCOME) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Income")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        errorMessage?.let { error ->
                            Text(
                                text = error,
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Button(
                            onClick = { viewModel.addCategory() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading
                        ) {
                            Text("Add Category")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Existing Categories",
                    style = MaterialTheme.typography.h6
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onEdit = { viewModel.editCategory(category) },
                            onDelete = { showDeleteDialog = category }
                        )
                        Divider()
                    }
                }
            }
        }
    }

    showDeleteDialog?.let { category ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Category") },
            text = { Text("Are you sure you want to delete '${category.name}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCategory(category.id)
                        showDeleteDialog = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
                text = category.name,
                style = MaterialTheme.typography.subtitle1
            )

            Text(
                text = category.type.name,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }

        Row {
            TextButton(onClick = onEdit) {
                Text("Edit")
            }

            TextButton(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}
