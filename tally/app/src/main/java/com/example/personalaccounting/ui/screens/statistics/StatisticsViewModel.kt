package com.example.personalaccounting.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalaccounting.data.repository.CategoryRepository
import com.example.personalaccounting.domain.model.CategoryExpense
import com.example.personalaccounting.domain.service.MonthlySummary
import com.example.personalaccounting.domain.service.StatisticsService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class StatisticsViewModel(
    private val statisticsService: StatisticsService,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categoryExpenses = MutableStateFlow<List<CategoryExpense>>(emptyList())
    val categoryExpenses: StateFlow<List<CategoryExpense>> = _categoryExpenses.asStateFlow()

    private val _monthlySummary = MutableStateFlow<MonthlySummary?>(null)
    val monthlySummary: StateFlow<MonthlySummary?> = _monthlySummary.asStateFlow()

    private val _selectedYear = MutableStateFlow(LocalDate.now().year)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    private val _selectedMonth = MutableStateFlow(LocalDate.now().monthValue)
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCategoryExpenses()
        loadMonthlySummary()
    }

    fun loadCategoryExpenses() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _categoryExpenses.value = statisticsService.getCategoryExpenses(
                    _selectedYear.value,
                    _selectedMonth.value
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMonthlySummary() {
        viewModelScope.launch {
            _monthlySummary.value = statisticsService.getMonthlySummary(
                _selectedYear.value,
                _selectedMonth.value
            )
        }
    }

    fun updateMonth(year: Int, month: Int) {
        _selectedYear.value = year
        _selectedMonth.value = month
        loadCategoryExpenses()
        loadMonthlySummary()
    }

    fun previousMonth() {
        if (_selectedMonth.value == 1) {
            _selectedYear.value -= 1
            _selectedMonth.value = 12
        } else {
            _selectedMonth.value -= 1
        }
        loadCategoryExpenses()
        loadMonthlySummary()
    }

    fun nextMonth() {
        if (_selectedMonth.value == 12) {
            _selectedYear.value += 1
            _selectedMonth.value = 1
        } else {
            _selectedMonth.value += 1
        }
        loadCategoryExpenses()
        loadMonthlySummary()
    }
}
