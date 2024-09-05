package com.gultekinahmetabdullah.sirdas.classes.dataclasses

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Document(
    override val uid: String = FirebaseAuth.getInstance().currentUser?.uid ?: "",
    override val id: Int = FirebaseFirestore.getInstance().collection("directories")
        .document().id.toInt(),
    override val name: String = "",
    override val createdAt: Long = System.currentTimeMillis(),
    override val updatedAt: Long = System.currentTimeMillis(),
    override val directory: Boolean = false,
    override val path: String,
    val downloadUrl: String,
) : FileObject() {
    constructor() : this(
        FirebaseAuth.getInstance().currentUser?.uid ?: "",
        FirebaseFirestore.getInstance().collection("directories").document().id.toInt(),
        "",
        System.currentTimeMillis(),
        System.currentTimeMillis(),
        false,
        "",
        ""
    )
}
