package com.gultekinahmetabdullah.sirdas.classes.dataclasses

data class Directory(
    override val uid: String = "",
    override val id: String = "",
    override val name: String = "",
    override val createdAt: Long = System.currentTimeMillis(),
    override val directory: Boolean = true,
    override val updatedAt: Long = System.currentTimeMillis(),
    val directoryContents: List<Directory> = emptyList()
) : FileObject() {
    constructor() : this(
        "",
        "",
        "",
        System.currentTimeMillis(),
        true,
        System.currentTimeMillis(),
        emptyList()
    )
}