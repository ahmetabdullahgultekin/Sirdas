package com.gultekinahmetabdullah.sirdas.viewmodels

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StatisticsViewModel : ViewModel() {

    private val _monthlyData = MutableStateFlow<List<Entry>>(emptyList())
    val monthlyData: StateFlow<List<Entry>> = _monthlyData

    private val _annualData = MutableStateFlow<List<Entry>>(emptyList())
    val annualData: StateFlow<List<Entry>> = _annualData

    private val _summaryData = MutableStateFlow<Map<String, String>>(emptyMap())
    val summaryData: StateFlow<Map<String, String>> = _summaryData

    init {
        // Load your data here
        loadMonthlyData()
        loadAnnualData()
        loadSummaryData()
    }

    private fun loadMonthlyData() {
        // Replace this with actual data loading logic
        _monthlyData.value = listOf(
            Entry(1f, 10f),
            Entry(2f, 20f),
            Entry(3f, 15f)
        )
    }

    private fun loadAnnualData() {
        // Replace this with actual data loading logic
        _annualData.value = listOf(
            Entry(1f, 100f),
            Entry(2f, 200f),
            Entry(3f, 150f)
        )
    }

    private fun loadSummaryData() {
        // Replace this with actual data loading logic
        _summaryData.value = mapOf(
            "Total Income" to "$50,000",
            "Total Expenses" to "$20,000",
            "Net Profit" to "$30,000"
        )
    }

    fun pieChartData(): List<PieEntry> {
        // Replace this with actual data
        return listOf(
            PieEntry(40f, "Cat 1"),
            PieEntry(30f, "Cat 2"),
            PieEntry(20f, "Cat 3"),
            PieEntry(10f, "Cat 4")
        )
    }
}
