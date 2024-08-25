package com.gultekinahmetabdullah.sirdas.screens.content.bottombar.calender

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen() {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val events = remember { mutableStateOf(getEventsForDate(selectedDate.value)) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Calendar Display
            CalendarView(
                selectedDate = selectedDate.value,
                onDateSelected = {
                    selectedDate.value = it
                    events.value = getEventsForDate(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Events List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(events.value) { event ->
                    EventCard(event = event)
                }
            }
        }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    // Basic Date Navigation (previous/next day)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onDateSelected(selectedDate.minusDays(1)) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Day")
        }
        Text(
            text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(onClick = { onDateSelected(selectedDate.plusDays(1)) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Day")
        }
    }

    // Calendar (Placeholder)
    // You can integrate a real calendar library here.
    Text(
        text = "Calendar goes here",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Time: ${event.time}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

data class Event(
    val title: String,
    val time: String,
    val description: String
)

fun getEventsForDate(date: LocalDate): List<Event> {
    // Mock events. Replace with real data fetching logic.
    return listOf(
        Event("Team Meeting", "09:00 AM", "Discuss project milestones and next steps."),
        Event("Design Review", "11:00 AM", "Review the latest design prototypes."),
        Event("Lunch with Sarah", "01:00 PM", "Discuss new marketing strategies."),
        Event("Code Review", "03:00 PM", "Review the pull requests from the team.")
    )
}
