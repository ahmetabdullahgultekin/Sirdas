package com.gultekinahmetabdullah.sirdas.classes.dataclasses

data class StudyTask(
    val id: Int,
    val title: String,
    val duration: Int, // Duration in minutes
    var isCompleted: Boolean = false
)

