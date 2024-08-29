package com.gultekinahmetabdullah.sirdas.screens.sign.signings

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gultekinahmetabdullah.sirdas.R
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.UserPreferences
import com.gultekinahmetabdullah.sirdas.classes.enums.Avatars
import com.gultekinahmetabdullah.sirdas.classes.enums.Languages
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.profile.AvatarPicker
import com.gultekinahmetabdullah.sirdas.viewmodels.PreferencesViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(onSignUp: () -> Unit, onGoBack: () -> Unit, viewModel: PreferencesViewModel) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    var profileIconID by remember { mutableIntStateOf(0) }
    val passwordVisibility = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scope2 = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Sign Up") },
                    navigationIcon = {
                        IconButton(onClick = { onGoBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Name Field
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Email Field
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Password Field
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }) {
                            Icon(
                                imageVector = if (passwordVisibility.value) ImageVector.vectorResource(
                                    id = R.drawable.baseline_visibility_off_24
                                ) else ImageVector.vectorResource(
                                    id = R.drawable.baseline_visibility_24
                                ),
                                contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                            )
                        }
                    }
                )

                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }) {
                            Icon(
                                imageVector = if (passwordVisibility.value) ImageVector.vectorResource(
                                    id = R.drawable.baseline_visibility_off_24
                                ) else ImageVector.vectorResource(
                                    id = R.drawable.baseline_visibility_24
                                ),
                                contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                            )
                        }
                    }
                )

                HorizontalDivider()

                AvatarPicker(
                    avatarList = Avatars.entries,
                    onAvatarSelected = { profileIconID = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    // Perform sign-up operation
                    var result: Boolean

                    scope2.launch {
                        if (password.value != confirmPassword.value) {
                            snackbarHostState.showSnackbar(
                                "Passwords do not match",
                                actionLabel = "OK",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                            return@launch
                        } else if (password.value.length < 6) {
                            snackbarHostState.showSnackbar(
                                "Password must be at least 6 characters long",
                                actionLabel = "OK",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                            return@launch
                        } else if (!isValidEmail(email.value)) {
                            snackbarHostState.showSnackbar(
                                "Invalid email format",
                                actionLabel = "OK",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                            return@launch
                        } else if (name.value.isBlank() || email.value.isBlank() || password.value.isBlank()) {
                            snackbarHostState.showSnackbar(
                                "Please fill in all fields",
                                actionLabel = "OK",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                            return@launch
                        }
                    }

                    result = false
                    scope.launch {

                        isLoading = true
                        result = signUp(
                            profileIconID,
                            name.value.trim(),
                            email.value.trim(),
                            password.value.trim()
                        )

                        if (result) {
                            viewModel.updateUserPreferences(
                                UserPreferences(
                                    userId = FirebaseAuth.getInstance().currentUser!!.uid,
                                    themeDark = false,
                                    language = Languages.ENGLISH,
                                    notificationEnabled = true,
                                    soundEnabled = true,
                                    vibrationEnabled = true
                                )
                            )
                            isLoading = false
                            onSignUp()
                        } else {
                            isLoading = false
                            snackbarHostState.showSnackbar(
                                "Sign-up failed",
                                actionLabel = "OK",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                            return@launch
                        }
                    }
                }) {
                    Text("Sign Up")
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

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFFFFFFF))
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

}


private suspend fun signUp(
    profileIconID: Int,
    name: String,
    email: String,
    password: String
): Boolean {

    return try {

        if (isValidEmail(email)) {
            // Proceed with Firebase Authentication using 'email'
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        } else {
            // Display an error message to the user about invalid email format
            throw Exception("Invalid email format")
        }
        // Save user data to Firestore
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(user!!.uid).set(
            hashMapOf(
                "uid" to user.uid,
                "profileIconID" to profileIconID,
                "name" to name,
                "email" to email,
                "password" to password
            )
        )
        true
    } catch (e: Exception) {
        // Handle sign-up error
        println(e.message)
        false
    }
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}