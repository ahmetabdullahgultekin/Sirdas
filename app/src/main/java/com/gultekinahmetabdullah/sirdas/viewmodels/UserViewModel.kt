package com.gultekinahmetabdullah.sirdas.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {

    var userProfile by mutableStateOf<User?>(null)

    init {
        try {
            loadUserProfile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            userProfile = getCurrentUserProfile()
        }
    }

    fun saveUserProfile(updatedProfile: User) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val success = updateUserProfile(userId, updatedProfile)
                if (success) {
                    userProfile = updatedProfile  // Update the local state with the new data
                } else {
                    // Handle the update failure (e.g., show a message to the user)
                }
            }
        }
    }

    private suspend fun getCurrentUserProfile(): User? {

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return null

        val db = FirebaseFirestore.getInstance()
        return try {
            val documentSnapshot = db.collection("users").document(userId).get().await()
            documentSnapshot.toObject(User::class.java)
        } catch (e: Exception) {
            // Handle error, e.g., log it or show a message to the user
            null
        }
    }


    private suspend fun updateUserProfile(userId: String, updatedProfile: User): Boolean {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("users").document(userId).set(updatedProfile).await()
            true  // Update successful
        } catch (e: Exception) {
            // Handle the error, e.g., log it or show a message to the user
            false  // Update failed
        }
    }
}
