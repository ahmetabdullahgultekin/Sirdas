package com.gultekinahmetabdullah.sirdas.dataclasses

data class Feedback(
    val userId: String,
    val userName : String,
    val feedbackText: String,
    val timestamp: Long
) {
    constructor() : this("", "", "", 0)
}