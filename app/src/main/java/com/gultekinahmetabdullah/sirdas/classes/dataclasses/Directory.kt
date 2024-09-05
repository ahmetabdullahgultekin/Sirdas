package com.gultekinahmetabdullah.sirdas.classes.dataclasses

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Directory(
    override val uid: String = FirebaseAuth.getInstance().currentUser?.uid ?: "",
    override val id: Int = FirebaseFirestore.getInstance().collection("directories")
        .document().id.toInt(),
    override val name: String = "",
    override val createdAt: Long = System.currentTimeMillis(),
    override val directory: Boolean = true,
    override val updatedAt: Long = System.currentTimeMillis(),
    val directoryContents: List<Directory> = emptyList()
) : FileObject() {
    constructor() : this(
        FirebaseAuth.getInstance().currentUser?.uid ?: "",
        FirebaseFirestore.getInstance().collection("directories").document().id.toInt(),
        "",
        System.currentTimeMillis(),
        true,
        System.currentTimeMillis(),
        emptyList()
    )
}