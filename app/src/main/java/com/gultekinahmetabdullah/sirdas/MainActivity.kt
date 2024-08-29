// MainActivity.kt
package com.gultekinahmetabdullah.sirdas

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.auth.FirebaseAuth
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.UserPreferences
import com.gultekinahmetabdullah.sirdas.classes.enums.Languages
import com.gultekinahmetabdullah.sirdas.screens.content.MainNavigator
import com.gultekinahmetabdullah.sirdas.screens.sign.SigningScreen
import com.gultekinahmetabdullah.sirdas.ui.theme.SirdasTheme
import com.gultekinahmetabdullah.sirdas.viewmodels.HealthViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.PreferencesViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.UserViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val isDarkTheme = remember { mutableStateOf(fetchUserPreferences().themeDark) }
            val isSignedIn = remember { mutableStateOf(checkIfSignedIn()) }

            /*            val onThemeChange: (Boolean) -> Unit = {
                            isDarkTheme.value = it
                        }*/


            val onSavePreferences: (UserPreferences) -> Unit = {
                setUserPreferences(it)
                setLocale(this, it.language.ordinal)
                isDarkTheme.value = it.themeDark
            }

            val userViewModel = UserViewModel()
            val preferencesViewModel = PreferencesViewModel()
            val healthViewModel = HealthViewModel()

            SirdasTheme(darkTheme = isDarkTheme.value) {

                if (isSignedIn.value) {
                    MainNavigator(
                        onSavePreferences = onSavePreferences,
                        onLogout = {
                            signOut()
                            setSignedOut()
                            isSignedIn.value = false
                        },
                        userViewModel = userViewModel,
                        preferencesViewModel = preferencesViewModel,
                        healthViewModel = healthViewModel
                    )
                } else {
                    SigningScreen(
                        onSignIn = {
                            setSignedIn()
                            isSignedIn.value = true
                        },
                        onSignUp = {
                            setSignedIn()
                            isSignedIn.value = true
                        },
                        viewModel = preferencesViewModel
                    )
                }
            }
        }
    }

    private fun checkIfSignedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        if (FirebaseAuth.getInstance().currentUser == null) {
            return false
        }
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

    // Update the user's preferences to the local of the device
    private fun setUserPreferences(userPreferences: UserPreferences) {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("user_id", userPreferences.userId)
            putBoolean("is_theme_dark", userPreferences.themeDark)
            putString("language", userPreferences.language.name)
            putBoolean("is_notification_enabled", userPreferences.notificationEnabled)
            putBoolean("is_sound_enabled", userPreferences.soundEnabled)
            putBoolean("is_vibration_enabled", userPreferences.vibrationEnabled)
            apply()
        }
    }

    // Fetch the user's preferences from the local of the device
    private fun fetchUserPreferences(): UserPreferences {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return UserPreferences(
            userId = sharedPreferences.getString("user_id", "") ?: "",
            themeDark = sharedPreferences.getBoolean("is_theme_dark", false),
            language = Languages.valueOf(
                sharedPreferences.getString("language", "ENGLISH") ?: "ENGLISH"
            ),
            notificationEnabled = sharedPreferences.getBoolean("is_notification_enabled", true),
            soundEnabled = sharedPreferences.getBoolean("is_sound_enabled", true),
            vibrationEnabled = sharedPreferences.getBoolean("is_vibration_enabled", false)
        )
    }

    private fun setLocale(context: Context, languageCode: Int) {
        val codeRepresentation = {
            when (languageCode) {
                0 -> "en"
                1 -> "tr"
                2 -> "de"
                3 -> "fr"
                4 -> "es"
                5 -> "it"
                6 -> "pt"
                7 -> "ru"
                8 -> "zh"
                9 -> "ja"
                10 -> "ko"
                11 -> "ar"
                12 -> "hi"
                else -> "en"
            }
        }
        val locale = Locale(codeRepresentation())
        Locale.setDefault(locale)
        val resources = context.resources
        val config = Configuration(resources.configuration)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        } else {
            config.locale = locale
        }

        resources.updateConfiguration(config, resources.displayMetrics)

        // Restart activity to apply the new language
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }

}