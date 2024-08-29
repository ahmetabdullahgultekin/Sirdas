package com.gultekinahmetabdullah.sirdas.viewmodels

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File

class DocumentViewModel : ViewModel() {

    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    var fileItems = mutableStateListOf<Document>()
        private set

    // Upload a file to Firebase Storage
    fun uploadFile(uri: Uri, fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val fileRef = storageReference.child("documents/${fileName.trim()}")
            fileRef.putFile(uri).await()
            val downloadUrl = fileRef.downloadUrl.await().toString()
            fileItems.add(Document(name = fileName, downloadUrl = downloadUrl))
        }
    }

    // Fetch all files from Firebase Storage
    fun fetchFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            val files = storageReference.child("documents").listAll().await()
            val fileList = files.items.map { storageRef ->
                val downloadUrl = storageRef.downloadUrl.await().toString()
                Document(name = storageRef.name, downloadUrl = downloadUrl)
            }
            fileItems.clear()
            fileItems.addAll(fileList)
        }
    }

    // Download a file using its download URL
    fun downloadFile(context: Context, fileName: String, downloadUrl: String) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(downloadUrl)
        val request = DownloadManager.Request(uri).apply {
            setTitle(fileName)
            setDescription("Downloading file...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }
        downloadManager.enqueue(request)
    }

    // Function to delete a file from Firebase Storage
    fun deleteFile(fileItem: Document) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Get reference to the file in Firebase Storage
                val fileRef = storageReference.child("documents/${fileItem.name}")
                // Delete the file
                fileRef.delete().await()
                // Remove the file from the list after deletion
                fileItems.remove(fileItem)
            } catch (e: Exception) {
                // Handle any errors during deletion (e.g., file not found)
                e.printStackTrace()
            }
        }
    }

    // Function to update the file name in Firebase Storage
    fun updateFileName(context: Context, oldFileItem: Document, newFileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Reference to the old file in Firebase Storage
                val oldFileRef = storageReference.child("documents/${oldFileItem.name}")
                // Reference to the new file in Firebase Storage
                val newFileRef = storageReference.child("documents/$newFileName")

                // Step 1: Download the file to a temporary local URI
                val localFile = File.createTempFile("tempFile", null, context.cacheDir)
                oldFileRef.getFile(localFile).await()

                // Step 2: Upload the file with the new name
                val fileUri = Uri.fromFile(localFile)
                newFileRef.putFile(fileUri).await()

                // Step 3: Get the new download URL
                val newDownloadUrl = newFileRef.downloadUrl.await().toString()

                // Step 4: Delete the old file
                oldFileRef.delete().await()

                // Step 5: Update the local list with the new file details
                val updatedFileItem =
                    oldFileItem.copy(name = newFileName, downloadUrl = newDownloadUrl)
                val index = fileItems.indexOf(oldFileItem)
                if (index != -1) {
                    fileItems[index] = updatedFileItem
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}



