package com.gultekinahmetabdullah.sirdas.classes.dataclasses

import java.util.Date

data class UserReadBook(
    val bookId: Int,
    val userId: String,
    val startDate: Date,
    val readDate: Date,
    val rating: Float,
    val pagesRead: Int,
    val finished: Boolean,
)
