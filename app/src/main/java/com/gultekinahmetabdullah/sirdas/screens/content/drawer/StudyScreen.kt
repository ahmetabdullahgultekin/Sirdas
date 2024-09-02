package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.StudyTask
import kotlinx.coroutines.delay

@Composable
fun StudyScreen() {
    var tasks by remember {
        mutableStateOf(
            listOf(
                StudyTask(1, "Read Chapter 1", 10),
                StudyTask(2, "Solve Math Problems", 30),
                StudyTask(3, "Write Essay", 40)
            )
        )
    }

    var currentTask by remember { mutableStateOf(tasks.first()) }

    var progress by remember { mutableFloatStateOf(1f) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Study Session",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CountdownTimer(
                initialTime = currentTask.duration,
                updateRemainingTime = { remainingTime ->
                    progress = remainingTime / (currentTask.duration * 60).toFloat()
                },
            ) {
                tasks = tasks.map {
                    if (it.id == currentTask.id) it.copy(isCompleted = true)
                    else it
                }
                currentTask = tasks.firstOrNull { !it.isCompleted } ?: currentTask
            }
            ProgressTracker(tasks = tasks, progress = progress)
        }


        TaskList(tasks = tasks) { updatedTask ->
            tasks = tasks.map {
                if (it.id == updatedTask.id) updatedTask else it
            }
        }
    }
}

@Composable
fun ProgressTracker(tasks: List<StudyTask>, progress: Float) {
    val completedTasks = tasks.count { it.isCompleted }
    val totalTasks = tasks.size
    //val progress = if (totalTasks > 0) completedTasks / totalTasks.toFloat() else 0f

    val springSpec = remember {
        SpringSpec(
            stiffness = Spring.DampingRatioNoBouncy,
            dampingRatio = Spring.DampingRatioNoBouncy,
            visibilityThreshold = 1f,
        )
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = springSpec,
        label = ""
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .size(200.dp),
            strokeWidth = 16.dp,
            color = Color.Blue,
        )
        Text(
            text = "Completed $completedTasks out of $totalTasks tasks",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun TaskList(
    tasks: List<StudyTask>,
    onTaskCompleted: (StudyTask) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(tasks) { task ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = {
                        onTaskCompleted(task.copy(isCompleted = it))
                    }
                )
                Text(
                    text = task.title,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CountdownTimer(
    initialTime: Int, // in minutes
    updateRemainingTime: (Int) -> Unit,
    onTimeUp: () -> Unit
) {
    var timeLeft by remember { mutableIntStateOf(initialTime * 60) } // convert to seconds

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1
            updateRemainingTime(timeLeft)
        } else {
            onTimeUp()
        }
    }

    Text(
        text = "Time Left: ${timeLeft / 60} minutes ${timeLeft % 60} seconds",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(16.dp)
    )
}
