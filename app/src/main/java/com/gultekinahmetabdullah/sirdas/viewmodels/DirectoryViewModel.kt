package com.gultekinahmetabdullah.sirdas.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Directory
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.FileObject

class DirectoryViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val directoriesCollection = firestore.collection("directories")

    private val storageRef = FirebaseStorage.getInstance().reference

    private val _directories = MutableLiveData<List<Directory>>()
    val directories: LiveData<List<Directory>> = _directories

    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val rootDirectory = "user_documents/$uid/"

    init {
        try {
            _directories.value = fetchDirectories().value
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Fetch all directories
    private fun fetchDirectories(): LiveData<List<Directory>> {
        val liveData = MutableLiveData<List<Directory>>()
        directoriesCollection.get().addOnSuccessListener { snapshot ->
            val directories = snapshot.documents.map { document ->
                document.toObject(Directory::class.java)!!
            }
            liveData.value = directories
        }
        return liveData
    }

    // Add or Update Directory
    fun addOrUpdateDirectory(fileObject: FileObject) {
        if (storageRef.child("${rootDirectory}${fileObject.path}")
                .putFile(Uri.EMPTY).isSuccessful
        ) {
            directoriesCollection.add(fileObject)
        } else {
            directoriesCollection.document(fileObject.path).set(fileObject).addOnCompleteListener {
                if (it.isSuccessful) {
                    // Handle success
                } else {
                    // Handle error
                }
            }
        }
    }

    // Delete Directory
    fun deleteDirectory(directoryPath: String) {
        directoriesCollection.document(directoryPath).delete()
    }
}
