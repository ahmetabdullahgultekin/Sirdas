package com.gultekinahmetabdullah.sirdas.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Directory
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.FileObject

class DirectoryViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val directoriesCollection = firestore.collection("directories")

    private val _directories = MutableLiveData<List<Directory>>()
    val directories: LiveData<List<Directory>> = _directories

    init {
        try {
            fetchDirectories()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Fetch all directories
    private fun fetchDirectories(): LiveData<List<Directory>> {
        val liveData = MutableLiveData<List<Directory>>()
        directoriesCollection.get().addOnSuccessListener { snapshot ->
            val directories = snapshot.documents.map { document ->
                document.toObject(Directory::class.java)!!.copy(id = document.id)
            }
            liveData.value = directories
        }
        return liveData
    }

    // Add or Update Directory
    fun addOrUpdateDirectory(fileObject: FileObject) {
        if (fileObject.id.isEmpty()) {
            directoriesCollection.add(fileObject)
        } else {
            directoriesCollection.document(fileObject.id).set(fileObject)
        }
    }

    // Delete Directory
    fun deleteDirectory(directoryId: String) {
        directoriesCollection.document(directoryId).delete()
    }
}
