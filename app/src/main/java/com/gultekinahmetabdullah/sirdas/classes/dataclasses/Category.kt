package com.gultekinahmetabdullah.sirdas.classes.dataclasses

import java.util.UUID

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val items: List<CategoryItem> = listOf(),
    val createdTime: Long = System.currentTimeMillis(),
    val lastEditedTime: Long = System.currentTimeMillis()
)



