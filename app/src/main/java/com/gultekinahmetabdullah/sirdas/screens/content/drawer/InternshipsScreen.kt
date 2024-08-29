package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Job

@Composable
fun JobsScreen() {
    val jobs = listOf(
        Job(
            1,
            "Software Engineer",
            "Tech Corp",
            "San Francisco, CA",
            "Full-time",
            "Develop and maintain software applications.",
            "2024-08-01"
        ),
        Job(
            2,
            "Marketing Intern",
            "Business Inc.",
            "New York, NY",
            "Internship",
            "Assist in marketing campaigns.",
            "2024-07-15"
        ),
        Job(
            3,
            "Product Manager",
            "Startup Co.",
            "Los Angeles, CA",
            "Full-time",
            "Manage product development.",
            "2024-07-01"
        ),
        Job(
            4,
            "Data Analyst",
            "Data Corp",
            "Chicago, IL",
            "Part-time",
            "Analyze data and generate reports.",
            "2024-06-15"
        ),
        Job(
            5,
            "Software Engineer",
            "Tech Corp",
            "San Francisco, CA",
            "Full-time",
            "Develop and maintain software applications.",
            "2024-08-01"
        ),
        Job(
            6,
            "Marketing Intern",
            "Business Inc.",
            "New York, NY",
            "Internship",
            "Assist in marketing campaigns.",
            "2024-07-15"
        ),
        Job(
            7,
            "Product Manager",
            "Startup Co.",
            "Los Angeles, CA",
            "Full-time",
            "Manage product development.",
            "2024-07-01"
        ),
        Job(
            8,
            "Data Analyst",
            "Data Corp",
            "Chicago, IL",
            "Part-time",
            "Analyze data and generate reports.",
            "2024-06-15"
        ),
        Job(
            9,
            "Software Engineer",
            "Tech Corp",
            "San Francisco, CA",
            "Full-time",
            "Develop and maintain software applications.",
            "2024-08-01"
        ),
        Job(
            10,
            "Marketing Intern",
            "Business Inc.",
            "New York, NY",
            "Internship",
            "Assist in marketing campaigns.",
            "2024-07-15"
        ),
    )

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filterOptions = listOf("All", "Full-time", "Part-time", "Internship")

    var selectedJob by remember { mutableStateOf<Job?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        if (selectedJob == null) {
            JobSearchAndFilter(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                selectedFilter = selectedFilter,
                onFilterChange = { selectedFilter = it },
                filterOptions = filterOptions
            )

            Spacer(modifier = Modifier.height(16.dp))

            JobList(
                jobs = jobs.filter { job ->
                    job.title.contains(searchQuery, ignoreCase = true) &&
                            (selectedFilter == "All" || job.type == selectedFilter)
                },
                onJobClick = { selectedJob = it }
            )
        } else {
            JobDetail(
                job = selectedJob!!,
                onBackClick = { selectedJob = null }
            )
        }
    }
}

@Composable
fun JobSearchAndFilter(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    filterOptions: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Search Jobs") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Filter")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
        ) {
            filterOptions.forEach { option ->
                DropdownMenuItem(onClick = { onFilterChange(option) },
                    text = {
                        Text(text = option)
                    })

            }
        }
    }
}

@Composable
fun JobDetail(
    job: Job,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = job.title, style = MaterialTheme.typography.bodyMedium)
        Text(text = job.company, style = MaterialTheme.typography.bodyMedium)
        Text(text = "${job.location} • ${job.type}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Posted on: ${job.postedDate}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = job.description, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun JobList(
    jobs: List<Job>,
    onJobClick: (Job) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(jobs) { job ->
            JobItem(job = job, onClick = onJobClick)
        }
    }
}

@Composable
fun JobItem(
    job: Job,
    onClick: (Job) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(job) },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job.title, style = MaterialTheme.typography.labelLarge)
            Text(text = job.company, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${job.location} • ${job.type}",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(text = "Posted on: ${job.postedDate}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
