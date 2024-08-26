// MainActivity.kt
package com.gultekinahmetabdullah.sirdas

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.auth.FirebaseAuth
import com.gultekinahmetabdullah.sirdas.screens.content.MainNavigator
import com.gultekinahmetabdullah.sirdas.screens.sign.SigningScreen
import com.gultekinahmetabdullah.sirdas.ui.theme.SirdasTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SirdasTheme {
                val isSignedIn = remember { mutableStateOf(checkIfSignedIn()) }
                if (isSignedIn.value) {
                    MainNavigator(onLogout = {
                        signOut()
                        setSignedOut()
                        isSignedIn.value = false
                    })
                } else {
                    SigningScreen(
                        onSignIn = {
                            setSignedIn()
                            isSignedIn.value = true
                        },
                        onSignUp = {
                            setSignedIn()
                            isSignedIn.value = true
                        })
                }
            }
        }
    }

    private fun checkIfSignedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_signed_in", false)
    }

    private fun setSignedIn() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("is_signed_in", true)
            apply()
        }
    }

    private fun setSignedOut() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("is_signed_in", false)
            apply()
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}