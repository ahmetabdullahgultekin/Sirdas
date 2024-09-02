// MainActivity.kt
package com.gultekinahmetabdullah.sirdas

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.google.firebase.auth.FirebaseAuth
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.UserPreferences
import com.gultekinahmetabdullah.sirdas.classes.enums.Languages
import com.gultekinahmetabdullah.sirdas.screens.content.MainNavigator
import com.gultekinahmetabdullah.sirdas.screens.sign.SigningScreen
import com.gultekinahmetabdullah.sirdas.screens.splash.SplashScreen
import com.gultekinahmetabdullah.sirdas.ui.theme.SirdasTheme
import com.gultekinahmetabdullah.sirdas.viewmodels.BookViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.DirectoryViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.DocumentViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.HealthViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.PreferencesViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            /*window.decorView.apply {
                systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            }*/

            val userPreferences = fetchUserPreferences()
            val isDarkTheme = remember { mutableStateOf(userPreferences.themeDark) }
            val isSignedIn = remember { mutableStateOf(checkIfSignedIn()) }

            val onSavePreferences: (UserPreferences) -> Unit = {
                setUserPreferences(it)
                setLocale(this, it.language.ordinal)
                isDarkTheme.value = it.themeDark
            }

            val userViewModel = UserViewModel()
            val preferencesViewModel = PreferencesViewModel(userPreferences)
            val healthViewModel = HealthViewModel()
            val bookViewModel = BookViewModel()
            val documentsViewModel = DocumentViewModel()
            val directoryViewModel = DirectoryViewModel()

            val isSplashScreenFinished = remember { mutableStateOf(false) }

            setLocale(this, userPreferences.language.ordinal)

            SirdasTheme(darkTheme = isDarkTheme.value) {

                if (!isSplashScreenFinished.value) {
                    SplashScreen(
                        onSplashScreenFinished = {
                            isSplashScreenFinished.value = true
                        }
                    )
                }

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
                        healthViewModel = healthViewModel,
                        bookViewModel = bookViewModel,
                        documentViewModel = documentsViewModel,
                        directoryViewModel = directoryViewModel
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
        try {
            var pref = false
            //val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

            val result = FirebaseAuth.getInstance().currentUser?.getIdToken(true)

            if (result != null) {
                pref = true
            } else {
                pref = false
                setSignedOut()
            }

            return pref
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun setSignedIn() {
        try {
            val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("is_signed_in", true)
                apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSignedOut() {
        try {
            val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("is_signed_in", false)
                apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun signOut() {
        try {
            FirebaseAuth.getInstance().signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Update the user's preferences to the local of the device
    private fun setUserPreferences(userPreferences: UserPreferences) {

        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Fetch the user's preferences from the local of the device
    private fun fetchUserPreferences(): UserPreferences {

        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
            return UserPreferences()
        }
    }

    private fun setLocale(context: Context, languageCode: Int) {

        try {
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

            config.setLocales(LocaleList(locale))

            resources.updateConfiguration(config, resources.displayMetrics)

            // Restart activity to apply the new language
            /*
            no need to restart
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}