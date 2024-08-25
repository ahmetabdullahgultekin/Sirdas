package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gultekinahmetabdullah.sirdas.dataclasses.Feedback
import com.gultekinahmetabdullah.sirdas.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun FeedbackScreen(viewModel: UserViewModel) {
    val feedbackText = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val attachment = remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "We value your feedback. Please provide details below:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Feedback Text Field
        OutlinedTextField(
            value = feedbackText.value,
            onValueChange = { feedbackText.value = it },
            label = { Text("Your Feedback") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 6,
            singleLine = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { /* Handle Done action */ }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Text Field (optional)
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Your Email (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Attachment Section
        Text(
            text = "Attach a screenshot or additional file (optional):",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = { /* Handle file attachment */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Attach File")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = attachment.value ?: "No file attached",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            // Submit feedback
            var result = false
            val feedback = Feedback(
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown",
                userName = viewModel.userProfile?.name ?: "Unknown",
                feedbackText = feedbackText.value,
                timestamp = System.currentTimeMillis()
            )
            if (!validateFeedback(feedback)) {
                return@Button
            }
            scope.launch {
                result = submitFeedback(feedback)
                if (result) {
                    feedbackText.value = ""
                    email.value = ""
                    attachment.value = null
                }
                isSubmitted = result
            }

        }) {
            Text("Submit Feedback")
        }

        if (isSubmitted != null) {
            Text(text = if (isSubmitted == true) "Feedback submitted" else "Submission failed")
        }
    }
}

fun validateFeedback(feedback: Feedback): Boolean {
    if (feedback.feedbackText.isBlank()) {
        // Handle empty feedback text
        println("Empty feedback text")
        return false
    }
    return true
}

private suspend fun submitFeedback(feedback: Feedback): Boolean {
    val db = FirebaseFirestore.getInstance()
    return try {
        db.collection("feedbacks")
            .add(feedback)
            .await()
        true
    } catch (e: Exception) {
        // Handle feedback submission error
        println(e.message)
        false
    }
}