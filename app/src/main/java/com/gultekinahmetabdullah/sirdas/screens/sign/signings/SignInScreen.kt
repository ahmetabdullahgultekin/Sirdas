package com.gultekinahmetabdullah.sirdas.screens.sign.signings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.gultekinahmetabdullah.sirdas.R
import com.gultekinahmetabdullah.sirdas.screens.sign.validateEmail
import com.gultekinahmetabdullah.sirdas.screens.sign.validatePassword
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SignInScreen(
    onSignIn: () -> Unit,
    navController: NavController,
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val scope2 = rememberCoroutineScope()
    val passwordVisibility = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            // Top right corner sign up button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
                TextButton(
                    onClick = { navController.navigate("forgotpassword") },
                    modifier = Modifier.align(Alignment.Top)
                ) {
                    Text(context.getString(R.string.forgot_password))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                // App Logo
                Image(
                    painter = painterResource(id = R.drawable.sirdas),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = context.getString(R.string.email)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = context.getString(R.string.password)) },
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
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
                                contentDescription = if (passwordVisibility.value)
                                    context.getString(R.string.hide_password)
                                else context.getString(R.string.show_password)

                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // Perform sign-in operation
                    var result: Boolean
                    scope.launch {
                        if (!validateEmail(email) || !validatePassword(password)) {
                            snackbarHostState.showSnackbar(
                                "Invalid email or password!",
                                actionLabel = "OK",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short,
                            )
                            return@launch
                        } else if (password.length < 6) {
                            snackbarHostState.showSnackbar(
                                "Password must be at least 6 characters!",
                                actionLabel = "OK",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                            return@launch
                        }
                    }

                    scope2.launch {
                        isLoading = true
                        result = signIn(email, password)
                        if (result) {
                            isLoading = false
                            onSignIn()
                        } else {
                            isLoading = false
                            snackbarHostState.showSnackbar(
                                "Sign-in failed!",
                                actionLabel = "OK",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                            return@launch
                        }
                    }

                }) {
                    Text(context.getString(R.string.sign_in))
                }

                TextButton(
                    onClick = { navController.navigate("signup") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(context.getString(R.string.sign_up))
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