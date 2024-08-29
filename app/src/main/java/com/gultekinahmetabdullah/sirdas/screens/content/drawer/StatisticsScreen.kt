package com.gultekinahmetabdullah.sirdas.screens.content.drawer
/*

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch


@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = remember { StatisticsViewModel() }) {
    var selectedTab by remember { mutableStateOf(StatisticsTab.MONTHLY) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle between Monthly and Annual stats
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatisticsTab.values().forEach { tab ->
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

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on the selected tab
        when (selectedTab) {
            StatisticsTab.MONTHLY -> MonthlyStatistics(viewModel = viewModel)
            StatisticsTab.ANNUAL -> AnnualStatistics(viewModel = viewModel)
        }
    }
}

@Composable
fun MonthlyStatistics(viewModel: StatisticsViewModel) {
    val monthlyData by remember { viewModel.monthlyData }.collectAsState()

    Column {
        // Line chart for monthly data
        LineChartView(data = monthlyData, modifier = Modifier.fillMaxWidth().height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // Pie chart to represent some categorical data
        PieChartView(data = viewModel.pieChartData(), modifier = Modifier.fillMaxWidth().height(300.dp))

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
        LineChartView(data = annualData, modifier = Modifier.fillMaxWidth().height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // Another pie chart or other graphical representation
        PieChartView(data = viewModel.pieChartData(), modifier = Modifier.fillMaxWidth().height(300.dp))

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
                setCenterText("Category Breakdown")
                setHoleColor(Color.WHITE)
                setTransparentCircleColor(Color.WHITE)
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

            val data = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter(chart))
                setValueTextSize(11f)
                setValueTextColor(Color.WHITE)
            }

            chart.data = data
            chart.invalidate()
        }
    )
}
*/
