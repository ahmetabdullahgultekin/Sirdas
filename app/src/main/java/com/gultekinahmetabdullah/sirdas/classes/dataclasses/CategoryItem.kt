package com.gultekinahmetabdullah.sirdas.classes.dataclasses

import java.util.UUID

data class CategoryItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val value: String,
    val createdTime: Long = System.currentTimeMillis(),
    val lastEditedTime: Long = System.currentTimeMillis()
)