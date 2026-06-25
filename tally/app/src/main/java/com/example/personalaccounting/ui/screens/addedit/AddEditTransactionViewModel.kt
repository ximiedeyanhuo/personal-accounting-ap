package com.example.personalaccounting.ui.screens.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.data.repository.TransactionRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.Transaction
import com.example.personalaccounting.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class AddEditTransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _type = MutableStateFlow(TransactionType.EXPENSE)
    val type: StateFlow<TransactionType> = _type.asStateFlow()

    private val _categoryId = MutableStateFlow<Long?>(null)
    val categoryId: StateFlow<Long?> = _categoryId.asStateFlow()

    private val _date = MutableStateFlow(LocalDate.now())
    val date: StateFlow<LocalDate> = _date.asStateFlow()

    private val _note = MutableStateFlow("")
    val note: StateFlow<String> = _note.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    private var editingTransactionId: Long? = null

    init {
        loadCategories()
    }

    fun loadTransaction(transactionId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val transaction = transactionRepository.getById(transactionId)
                transaction?.let {
                    editingTransactionId = it.id
                    _amount.value = it.amount.toString()
                    _type.value = it.type
                    _categoryId.value = it.categoryId
                    _date.value = it.date
                    _note.value = it.note
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getByType(_type.value)
        }
    }

    fun updateAmount(amount: String) {
        _amount.value = amount
        _errorMessage.value = null
    }

    fun updateType(type: TransactionType) {
        _type.value = type
        loadCategories()
    }

    fun updateCategoryId(categoryId: Long?) {
        _categoryId.value = categoryId
    }

    fun updateDate(date: LocalDate) {
        _date.value = date
    }

    fun updateNote(note: String) {
        _note.value = note
    }

    fun saveTransaction() {
        val amountValue = _amount.value.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            _errorMessage.value = "Amount is required"
            return
        }

        if (_categoryId.value == null) {
            _errorMessage.value = "Category is required"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val transaction = Transaction(
                    id = editingTransactionId ?: 0,
                    amount = amountValue,
                    type = _type.value,
                    categoryId = _categoryId.value!!,
                    date = _date.value,
                    note = _note.value,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )

                if (editingTransactionId != null) {
                    transactionRepository.update(transaction)
                } else {
                    transactionRepository.add(transaction)
                }

                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSuccess() {
        _isSuccess.value = false
    }
}
