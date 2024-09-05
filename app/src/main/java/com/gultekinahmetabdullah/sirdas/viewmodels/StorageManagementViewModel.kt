package com.gultekinahmetabdullah.sirdas.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage

class StorageManagementViewModel : ViewModel() {

    private val storageRef = FirebaseStorage.getInstance().reference

    // Function to upload a file to a directory
    fun uploadFileToDirectory(
        directoryName: String,
        fileUri: Uri,
        fileName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val directoryRef = storageRef.child("user_documents/$directoryName/$fileName")

        val uploadTask = directoryRef.putFile(fileUri)
        uploadTask.addOnSuccessListener {
            Log.d("Firebase", "File uploaded successfully to $directoryName/$fileName")
            onSuccess()
        }.addOnFailureListener {
            Log.e("Firebase", "File upload failed: ${it.message}")
            onFailure(it.message ?: "Unknown error")
        }
    }

    // Function to delete a file in a directory
    fun deleteFile(
        directory: String,
        fileName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val fileRef = storageRef.child("user_documents/$directory/$fileName")

        fileRef.delete().addOnSuccessListener {
            Log.d("Firebase", "File deleted: $fileName")
            onSuccess()
        }.addOnFailureListener {
            Log.e("Firebase", "File deletion failed: ${it.message}")
            onFailure(it.message ?: "Unknown error")
        }
    }

    // Function to update (replace) a file in a directory
    fun updateFile(
        directory: String,
        fileUri: Uri,
        fileName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        uploadFileToDirectory(directory, fileUri, fileName, onSuccess, onFailure)
    }

    // Function to list files in a directory
    fun listFilesInDirectory(
        directoryName: String,
        onSuccess: (List<String>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val directoryRef = storageRef.child(directoryName)

        directoryRef.listAll().addOnSuccessListener { listResult ->
            val fileList = listResult.items.map { it.name }
            Log.d("Firebase", "Files in directory $directoryName: $fileList")
            onSuccess(fileList)
        }.addOnFailureListener {
            Log.e("Firebase", "Failed to list files: ${it.message}")
            onFailure(it.message ?: "Unknown error")
        }
    }

    // Function to delete all files in a directory (simulates deleting a directory)
    fun deleteDirectory(directoryName: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val directoryRef = storageRef.child(directoryName)

        directoryRef.listAll().addOnSuccessListener { listResult ->
            for (fileRef in listResult.items) {
                fileRef.delete().addOnSuccessListener {
                    Log.d("Firebase", "Deleted file: ${fileRef.name}")
                }.addOnFailureListener {
                    Log.e("Firebase", "Failed to delete file: ${it.message}")
                }
            }
            onSuccess()
        }.addOnFailureListener {
            Log.e("Firebase", "Failed to list directory: ${it.message}")
            onFailure(it.message ?: "Unknown error")
        }
    }
}
