package com.gultekinahmetabdullah.sirdas.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.gultekinahmetabdullah.sirdas.dataclasses.Document
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DocumentViewModel : ViewModel() {

    private val _documents = MutableStateFlow<List<Document>>(emptyList())
    val documents: StateFlow<List<Document>> = _documents

    fun uploadDocument(fileUri: Uri) {
        viewModelScope.launch {
            val fileName = fileUri.lastPathSegment ?: "document_${System.currentTimeMillis()}.pdf"
            val fileUrl = uploadDocumentDB(fileUri, fileName)
            if (fileUrl != null) {
                _documents.value = _documents.value + Document(fileName, fileUrl)
            }
        }
    }

    fun deleteDocument(fileName: String) {
        viewModelScope.launch {
            val success = deleteDocumentDB(fileName)
            if (success) {
                _documents.value = _documents.value.filter { it.name != fileName }
            }
        }
    }

    fun viewDocument(uri: String) {
        // Logic to view document (e.g., opening a PDF viewer)
    }

    private suspend fun uploadDocumentDB(fileUri: Uri, fileName: String): String? {
        val storageRef = FirebaseStorage.getInstance().reference.child("documents/$fileName")
        return try {
            storageRef.putFile(fileUri).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun deleteDocumentDB(fileName: String): Boolean {
        val storageRef = FirebaseStorage.getInstance().reference.child("documents/$fileName")
        return try {
            storageRef.delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}



