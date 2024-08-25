package com.gultekinahmetabdullah.sirdas.dataclasses

data class User(
    val userId: String,
    val name: String,
    val email: String,
    val password: String
) {
    constructor() : this("", "", "", "") {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
