package com.gultekinahmetabdullah.sirdas.classes.dataclasses

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class FileObject {
    open val uid: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    open val id: Int =
        FirebaseFirestore.getInstance().collection("directories").document().id.toInt()
    open val name: String = ""
    open val createdAt: Long = System.currentTimeMillis()
    open val updatedAt: Long = System.currentTimeMillis()
    open val directory: Boolean = false
    open val path: String = "user_documents/${FirebaseAuth.getInstance().currentUser?.uid ?: ""}/"
}