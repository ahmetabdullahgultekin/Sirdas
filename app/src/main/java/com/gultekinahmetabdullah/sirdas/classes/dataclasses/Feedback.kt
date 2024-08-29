package com.gultekinahmetabdullah.sirdas.classes.dataclasses

data class Feedback(
    val userId: String,
    val userName: String,
    val feedbackText: String,
    val timestamp: Long
) {
    constructor() : this("", "", "No feedbacks yet.", 0)
}