package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.StatisticsTab
import com.gultekinahmetabdullah.sirdas.viewmodels.StatisticsViewModel
import kotlinx.coroutines.launch


@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = remember { StatisticsViewModel() }) {
    var selectedTab by remember { mutableStateOf(StatisticsTab.MONTHLY) }
    val coroutineScope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("My Weight") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        /*Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )*/

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable { expanded = true },
            contentAlignment = Alignment.CenterStart
        ) {

            Text(
                text = selectedCategory,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    // Handle dismiss request
                    expanded = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(
                    text = { Text("Books Read") },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "Books Read"
                            expanded = false
                        }
                    },
                )
                DropdownMenuItem(
                    text = { Text("Book Pages Read") },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "Book Pages Read"
                            expanded = false
                        }
                    },
                )
                DropdownMenuItem(
                    text = { Text("Movies Watched") },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "Movies Watched"
                            expanded = false
                        }
                    },
                )
                DropdownMenuItem(
                    text = { Text("My Weight") },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "My Weight"
                            expanded = false
                        }
                    },
                )
                DropdownMenuItem(
                    text = { Text("My Height") },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "My Height"
                            expanded = false
                        }
                    },
                )
                DropdownMenuItem(
                    text = { Text("My BMI") },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "My BMI"
                            expanded = false
                        }
                    },
                )
                DropdownMenuItem(
                    text = { Text("My Body Fat") },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "My Body Fat"
                            expanded = false
                        }
                    },
                )
                DropdownMenuItem(
                    text = { Text("My Muscle Mass") },
                    onClick = {
                        coroutineScope.launch {
                            selectedCategory = "My Muscle Mass"
                            expanded = false
                        }
                    },
                )
            }
        }

        // Toggle between Monthly and Annual stats
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            StatisticsTab.entries.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = {
                        coroutineScope.launch {
                            selectedTab = tab
                        }
                    },
                    text = { Text(tab.name) }
                )
            }
        }

        // Content based on the selected tab
        when (selectedTab) {
            StatisticsTab.MONTHLY -> MonthlyStatistics(viewModel = viewModel)
            StatisticsTab.ANNUAL -> AnnualStatistics(viewModel = viewModel)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MonthlyStatistics(viewModel: StatisticsViewModel) {
    val monthlyData by remember { viewModel.monthlyData }.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Line chart for monthly data
        LineChartView(
            data = monthlyData, modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pie chart to represent some categorical data
        PieChartView(
            data = viewModel.pieChartData(), modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Summary section
        SummarySection(viewModel = viewModel)
    }
}

@Composable
fun AnnualStatistics(viewModel: StatisticsViewModel) {
    val annualData by remember { viewModel.annualData }.collectAsState()

    Column {
        // Line chart for annual data
        LineChartView(
            data = annualData, modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Another pie chart or other graphical representation
        PieChartView(
            data = viewModel.pieChartData(), modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Summary section
        SummarySection(viewModel = viewModel)
    }
}

@Composable
fun SummarySection(viewModel: StatisticsViewModel) {
    val summary by remember { viewModel.summaryData }.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Summary", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(8.dp))

            summary.forEach { item ->
                Text(
                    text = "${item.key}: ${item.value}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun LineChartView(data: List<Entry>, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                setBackgroundColor(ColorTemplate.rgb("#f0f0f0"))
                animateX(1500)

                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)

                axisLeft.setDrawGridLines(false)
                axisRight.isEnabled = false

                legend.form = Legend.LegendForm.LINE
            }
        },
        update = { chart ->
            val dataSet = LineDataSet(data, "Statistics").apply {
                color = ColorTemplate.getHoloBlue()
                setCircleColor(ColorTemplate.getHoloBlue())
                lineWidth = 2f
                circleRadius = 4f
                fillAlpha = 65
                fillColor = ColorTemplate.getHoloBlue()
                highLightColor = ColorTemplate.rgb("#1D1D1D")
                setDrawCircleHole(false)
            }
            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
}

@Composable
fun PieChartView(data: List<PieEntry>, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                setUsePercentValues(true)
                setExtraOffsets(5f, 10f, 5f, 5f)
                setDragDecelerationFrictionCoef(0.95f)
                centerText = "Category Breakdown"
                setHoleColor(ColorTemplate.rgb("#f0f0f0"))
                setTransparentCircleColor(ColorTemplate.rgb("#f0f0f0"))
                setTransparentCircleAlpha(110)
                holeRadius = 58f
                transparentCircleRadius = 61f
                setDrawCenterText(true)
                rotationAngle = 0f
                isRotationEnabled = true
                isHighlightPerTapEnabled = true
                animateY(1400, Easing.EaseInOutQuad)

                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.setDrawInside(false)
                legend.xEntrySpace = 7f
                legend.yEntrySpace = 0f
                legend.yOffset = 0f
            }
        },
        update = { chart ->
            val dataSet = PieDataSet(data, "Categories").apply {
                sliceSpace = 3f
                selectionShift = 5f
                colors = ColorTemplate.MATERIAL_COLORS.toList()
            }

            val datas = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter(chart))
                setValueTextSize(11f)
                setValueTextColor(ColorTemplate.rgb("#1D1D1D"))
            }

            chart.data = datas
            chart.invalidate()
        }
    )
}
