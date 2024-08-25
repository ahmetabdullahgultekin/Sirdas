// app/src/main/java/com/gultekinahmetabdullah/sirdas/screens/SignInScreen.kt
package com.gultekinahmetabdullah.sirdas.screens.sign

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gultekinahmetabdullah.sirdas.screens.sign.signings.ForgotPasswordScreen
import com.gultekinahmetabdullah.sirdas.screens.sign.signings.SignInScreen
import com.gultekinahmetabdullah.sirdas.screens.sign.signings.SignUpScreen

@Composable
fun SigningScreen(onSignIn: () -> Unit, onSignUp: () -> Unit) {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            NavHost(
                navController = navController,
                startDestination = "signin",
                modifier = Modifier.padding(padding)
            )     {
                composable("signin") { SignInScreen(onSignIn, navController) }
                composable("signup") { SignUpScreen(onSignUp, onGoBack = { navController.popBackStack() }) }
                composable("forgotpassword") { ForgotPasswordScreen(onGoBack = {
                    navController.popBackStack()
                }) }
            }
        }
    )
}

fun validatePassword(password: String): Boolean {
    if (password.length < 6) {
        // Handle invalid password
        println("Invalid password")
        return false
    }
    return true
}

fun validateEmail(email: String): Boolean {
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
        // Handle invalid email
        println("Invalid email")
        return false
    }
    return true
}






