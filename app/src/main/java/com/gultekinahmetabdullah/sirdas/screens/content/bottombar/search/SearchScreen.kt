package com.gultekinahmetabdullah.sirdas.screens.content.bottombar.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen() {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    val searchResults = remember { mutableStateOf(listOf<SearchResult>()) }
    val contentTypes = listOf("PDF", "Image", "Note", "Video", "Audio")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Input Field
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                searchResults.value = performSearch(it.text)
            },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Chips for Content Types
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            contentTypes.forEach { type ->
                FilterChip(
                    selected = false,
                    onClick = { /* Implement filter logic */ },
                    label = { Text(type) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Results
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(searchResults.value) { result ->
                SearchResultCard(result = result)
            }
        }
    }
}

@Composable
fun SearchResultCard(result: SearchResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = result.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Type: ${result.type}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = result.description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

data class SearchResult(
    val title: String,
    val type: String,
    val description: String
)

fun performSearch(query: String): List<SearchResult> {
    // Mock search results. Replace with real search logic.
    return listOf(
        SearchResult("Project Plan", "PDF", "A detailed project plan document."),
        SearchResult("Design Mockup", "Image", "UI mockups for the new feature."),
        SearchResult("Meeting Notes", "Note", "Notes from the team meeting."),
        SearchResult("Tutorial Video", "Video", "A tutorial on how to use the new tool."),
        SearchResult("Podcast Episode", "Audio", "Latest episode of the tech podcast.")
    ).filter { it.title.contains(query, ignoreCase = true) }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
