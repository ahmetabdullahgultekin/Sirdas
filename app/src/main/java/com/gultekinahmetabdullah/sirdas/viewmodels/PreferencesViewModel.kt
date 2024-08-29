package com.gultekinahmetabdullah.sirdas.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.UserPreferences
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PreferencesViewModel() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("user-preferences")

    private val _userPreferences = mutableStateOf<UserPreferences?>(null)
    val userPreferences: State<UserPreferences?> get() = _userPreferences


    init {

        try {
            // Load the user's preferences
            viewModelScope.launch {
                fetchUserPreferences(FirebaseAuth.getInstance().currentUser?.uid ?: "")

                // Update the theme based on the user's preferences from the database
                //onThemeChange(_userPreferences.value?.themeDark ?: false)

                /*                if (_userPreferences.value == null) {
                                    // Load the user's preferences from the MainActivity locally
                                    val userPreferencesFromLocal = MainActivity().fetchUserPreferences()
                                    _userPreferences.value = UserPreferences(
                                        userId = userPreferencesFromLocal.userId,
                                        isThemeDark = userPreferencesFromLocal.isThemeDark,
                                        language = userPreferencesFromLocal.language,
                                        isNotificationEnabled = userPreferencesFromLocal.isNotificationEnabled,
                                        isSoundEnabled = userPreferencesFromLocal.isSoundEnabled,
                                        isVibrationEnabled = userPreferencesFromLocal.isVibrationEnabled
                                    )
                                }*/
            }
        } catch (e: Exception) {
            Log.e("PreferencesViewModel", "Error fetching preferences.", e)
        }
    }

    // Fetch the user's preferences
    private suspend fun fetchUserPreferences(userId: String) {
        try {
            val documentSnapshot = usersCollection.document(userId).get().await()
            _userPreferences.value = documentSnapshot.toObject(UserPreferences::class.java)
        } catch (e: Exception) {
            Log.e("PreferencesViewModel", "Error fetching preferences.", e)
        }
    }

    // Update the user's preferences
    suspend fun updateUserPreferences(userPreferences: UserPreferences) {
        try {
            //MainActivity().setUserPreferences(userPreferences)
            _userPreferences.value = userPreferences
            usersCollection.document(userPreferences.userId).set(userPreferences).await()
        } catch (e: Exception) {
            Log.e("PreferencesViewModel", "Error updating preferences.", e)
        }
    }
}
