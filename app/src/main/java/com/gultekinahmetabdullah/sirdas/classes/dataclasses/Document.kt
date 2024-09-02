package com.gultekinahmetabdullah.sirdas.classes.dataclasses

data class Document(
    override val uid: String,
    override val id: String,
    override val name: String,
    override val createdAt: Long = System.currentTimeMillis(),
    override val updatedAt: Long = System.currentTimeMillis(),
    override val directory: Boolean = false,
    val path: String,
    val downloadUrl: String,
) : FileObject() {
    constructor() : this(
        "",
        "",
        "",
        System.currentTimeMillis(),
        System.currentTimeMillis(),
        false,
        "",
        ""
    )
}
