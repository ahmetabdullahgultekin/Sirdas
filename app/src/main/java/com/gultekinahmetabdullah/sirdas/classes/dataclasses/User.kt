package com.gultekinahmetabdullah.sirdas.classes.dataclasses

data class User(
    val userId: String,
    val profileIconID: Int,
    val name: String,
    val email: String,
    val password: String
) {
    constructor() : this("", 0, "", "", "") {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
