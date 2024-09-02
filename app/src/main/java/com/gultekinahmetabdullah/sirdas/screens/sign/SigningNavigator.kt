// app/src/main/java/com/gultekinahmetabdullah/sirdas/screens/SignInScreen.kt
package com.gultekinahmetabdullah.sirdas.screens.sign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gultekinahmetabdullah.sirdas.screens.sign.signings.ForgotPasswordScreen
import com.gultekinahmetabdullah.sirdas.screens.sign.signings.SignInScreen
import com.gultekinahmetabdullah.sirdas.screens.sign.signings.SignUpScreen
import com.gultekinahmetabdullah.sirdas.viewmodels.PreferencesViewModel

@Composable
fun SigningScreen(onSignIn: () -> Unit, onSignUp: () -> Unit, viewModel: PreferencesViewModel) {
    val navController = rememberNavController()



    NavHost(
        navController = navController,
        startDestination = "signin",
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        composable("signin") {
            SignInScreen(
                onSignIn,
                navController,
            )
        }
        composable("signup") {
            SignUpScreen(
                onSignUp,
                onGoBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable("forgotpassword") {
            ForgotPasswordScreen(onGoBack = {
                navController.popBackStack()
            })
        }
    }
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






