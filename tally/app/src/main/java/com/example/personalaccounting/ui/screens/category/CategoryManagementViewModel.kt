package com.example.personalaccounting.ui.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.domain.model.Category
import com.example.personalaccounting.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryManagementViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _type = MutableStateFlow(TransactionType.EXPENSE)
    val type: StateFlow<TransactionType> = _type.asStateFlow()

    private val _icon = MutableStateFlow("")
    val icon: StateFlow<String> = _icon.asStateFlow()

    private val _color = MutableStateFlow("")
    val color: StateFlow<String> = _color.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    private var editingCategoryId: Long? = null

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _categories.value = categoryRepository.getAll()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateName(name: String) {
        _name.value = name
        _errorMessage.value = null
    }

    fun updateType(type: TransactionType) {
        _type.value = type
    }

    fun updateIcon(icon: String) {
        _icon.value = icon
    }

    fun updateColor(color: String) {
        _color.value = color
    }

    fun addCategory() {
        if (_name.value.isBlank()) {
            _errorMessage.value = "Category name is required"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val category = Category(
                    name = _name.value,
                    icon = _icon.value,
                    color = _color.value,
                    type = _type.value,
                    isDefault = false
                )

                categoryRepository.insert(category)
                _isSuccess.value = true
                loadCategories()
                clearForm()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateCategory() {
        if (_name.value.isBlank()) {
            _errorMessage.value = "Category name is required"
            return
        }

        if (editingCategoryId == null) {
            _errorMessage.value = "No category selected for editing"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val category = Category(
                    id = editingCategoryId!!,
                    name = _name.value,
                    icon = _icon.value,
                    color = _color.value,
                    type = _type.value,
                    isDefault = false
                )

                categoryRepository.update(category)
                _isSuccess.value = true
                loadCategories()
                clearForm()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                categoryRepository.delete(categoryId)
                loadCategories()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun editCategory(category: Category) {
        editingCategoryId = category.id
        _name.value = category.name
        _type.value = category.type
        _icon.value = category.icon
        _color.value = category.color
    }

    fun clearForm() {
        editingCategoryId = null
        _name.value = ""
        _type.value = TransactionType.EXPENSE
        _icon.value = ""
        _color.value = ""
        _errorMessage.value = null
    }

    fun resetSuccess() {
        _isSuccess.value = false
    }
}
