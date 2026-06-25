package com.example.personalaccounting.ui.screens.addedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.personalaccounting.domain.model.TransactionType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun AddEditTransactionScreen(
    transactionId: Long? = null,
    onNavigateBack: () -> Unit,
    viewModel: AddEditTransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val amount by viewModel.amount.collectAsState()
    val type by viewModel.type.collectAsState()
    val categoryId by viewModel.categoryId.collectAsState()
    val date by viewModel.date.collectAsState()
    val note by viewModel.note.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(transactionId) {
        transactionId?.let {
            viewModel.loadTransaction(it)
        }
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onNavigateBack()
            viewModel.resetSuccess()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.updateDate(selectedDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (transactionId != null) "Edit Transaction" else "Add Transaction") },
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { viewModel.updateAmount(it) },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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
                        label = { Text("Expense") },
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        selected = type == TransactionType.INCOME,
                        onClick = { viewModel.updateType(TransactionType.INCOME) },
                        label = { Text("Income") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Category",
                    style = MaterialTheme.typography.subtitle1
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateCategoryId(category.id) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = categoryId == category.id,
                                onClick = { viewModel.updateCategoryId(category.id) }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                                contentDescription = "Select Date"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { viewModel.updateNote(it) },
                    label = { Text("Note (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

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
                    onClick = { viewModel.saveTransaction() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("Save")
                }
            }
        }
    }
}
