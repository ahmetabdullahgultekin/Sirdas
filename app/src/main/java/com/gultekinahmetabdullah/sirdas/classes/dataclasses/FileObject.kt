package com.gultekinahmetabdullah.sirdas.classes.dataclasses

open class FileObject {
    open val uid: String = ""
    open val id: String = ""
    open val name: String = ""
    open val createdAt: Long = System.currentTimeMillis()
    open val updatedAt: Long = System.currentTimeMillis()
    open val directory: Boolean = false
}