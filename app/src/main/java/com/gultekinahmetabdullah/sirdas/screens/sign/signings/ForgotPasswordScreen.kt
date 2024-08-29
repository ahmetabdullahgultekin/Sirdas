package com.gultekinahmetabdullah.sirdas.screens.sign.signings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(onGoBack: () -> Unit) {
    val email = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Forgot Password") },
                    navigationIcon = {
                        IconButton(onClick = { onGoBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Enter your email address to receive a password reset link.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Email Field
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Reset Password Button
                Button(
                    onClick = { sendResetPasswordEmail(email.value, snackbarHostState) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Reset Password")
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .zIndex(2f)
                .align(Alignment.TopCenter)
                .padding(16.dp)
        )
    }
}

fun sendResetPasswordEmail(value: String, snackbarHostState: SnackbarHostState) {
    // Send reset password email
    FirebaseAuth.getInstance().sendPasswordResetEmail(value)
        .addOnCompleteListener { task ->
            CoroutineScope(Dispatchers.Main).launch {
                if (task.isSuccessful) {
                    snackbarHostState.showSnackbar(
                        "Password reset email sent successfully.",
                        actionLabel = "OK",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                } else {
                    snackbarHostState.showSnackbar(
                        "Failed to send password reset email.",
                        actionLabel = "OK",
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
}
