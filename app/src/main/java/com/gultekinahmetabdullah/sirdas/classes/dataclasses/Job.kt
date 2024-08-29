package com.gultekinahmetabdullah.sirdas.classes.dataclasses

data class Job(
    val id: Int,
    val title: String,
    val company: String,
    val location: String,
    val type: String, // e.g., "Full-time", "Internship", etc.
    val description: String,
    val postedDate: String
)
