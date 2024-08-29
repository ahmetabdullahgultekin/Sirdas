package com.gultekinahmetabdullah.sirdas.classes.dataclasses

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val description: String,
    val publishedDate: String,
    val isPursued: Boolean = false
)

