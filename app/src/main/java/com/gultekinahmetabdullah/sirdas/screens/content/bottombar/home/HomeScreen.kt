package com.gultekinahmetabdullah.sirdas.screens.content.bottombar.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.gultekinahmetabdullah.sirdas.R
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Feedback
import com.gultekinahmetabdullah.sirdas.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun HomeScreen(viewModel: UserViewModel) {
    val missions = listOf("Complete Kotlin project", "Review pull requests", "Plan next sprint")
    val stats = mapOf("Tasks Completed" to 12, "Tasks Pending" to 3)
    val lastAction = "Created a new task: 'Finish UI design'"
    val scope = rememberCoroutineScope()
    var feedbacks by remember { mutableStateOf(emptyList<Feedback>()) }

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            item {
                WelcomeCard(viewModel.userProfile?.name ?: "", context)
            }

            scope.launch {
                feedbacks = getFeedbacks()

                if (feedbacks.isEmpty()) {
                    feedbacks = listOf(
                        Feedback()
                    )
                }
            }

            item {
                FeedbackCard(
                    feedbacks = feedbacks
                )
            }

            /*
            item {
                HomeCard(title = "Today's Mission", content = missions.joinToString("\n"))
            }

            item {
                StatsCard(title = "Stats", stats = stats)
            }

            item {
                HomeCard(title = "Your Last Action", content = lastAction)
            }

            item {
                HomeCard(
                    title = "Upcoming Deadlines",
                    content = "Design review: Aug 25\nCode freeze: Aug 30"
                )
            }

            item {
                HomeCard(title = "Focus Time", content = "Next focus session starts in 2 hours.")
            }

            item {
                HomeCard(
                    title = "Team Progress",
                    content = "Team Alpha: 85% complete\nTeam Beta: 70% complete"
                )
            }

            item {
                HomeCard(
                    title = "New Announcements",
                    content = "1. Company meeting on Aug 27.\n2. New feature rollout next week."
                )
            }*/
        }
    }
}

@Composable
fun WelcomeCard(name: String, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = context.getString(R.string.welcome) + " $name",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "When dynamic data is available, new cards will display your daily summary.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun HomeCard(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun StatsCard(title: String, stats: Map<String, Int>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            stats.forEach { (key, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = key)
                    Text(text = value.toString())
                }
            }
        }
    }
}

@Composable
fun FeedbackCard(feedbacks: List<Feedback>) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Recent Feedbacks",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (feedbacks.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(CenterHorizontally)
                )
            }

            feedbacks.forEach { feedback ->
                Text(
                    text = feedback.feedbackText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}


suspend fun getFeedbacks(): List<Feedback> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("feedbacks").get().await()
        snapshot.documents.map { document ->
            document.toObject(Feedback::class.java)!!
        }
    } catch (e: Exception) {
        // Handle retrieval error
        println(e.message)
        emptyList()
    }
}