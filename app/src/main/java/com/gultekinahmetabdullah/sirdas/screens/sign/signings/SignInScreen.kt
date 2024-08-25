package com.gultekinahmetabdullah.sirdas.screens.sign.signings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.gultekinahmetabdullah.sirdas.screens.sign.validateEmail
import com.gultekinahmetabdullah.sirdas.screens.sign.validatePassword
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SignInScreen(onSignIn: () -> Unit, navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var isSuccessful by remember { mutableStateOf<Boolean?>(null) }
    val passwordVisibility = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Top right corner sign up button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            TextButton(
                onClick = { navController.navigate("signup") },
                modifier = Modifier.align(Alignment.Top)
            ) {
                Text("Sign Up")
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                imageVector = Icons.Filled.Face,
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Perform sign-in operation
                var result: Boolean
                if (!validateEmail(email) || !validatePassword(password)) {
                    return@Button
                }
                scope.launch {
                    result = signIn(email, password)
                    if (result) {
                        onSignIn()
                    }
                    isSuccessful = result
                }

            }) {
                Text("Sign In")
            }

            if (isSuccessful != null) {
                Text(text = if (isSuccessful == true) "Sign-in successful" else "Sign-in failed")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate("forgotpassword") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Forgot Password?")
            }
        }
    }
}

private suspend fun signIn(email: String, password: String): Boolean {
    return try {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password.trim()).await()
        true
    } catch (e: Exception) {
        // Handle sign-in error
        println(e.message)
        false
    }
}