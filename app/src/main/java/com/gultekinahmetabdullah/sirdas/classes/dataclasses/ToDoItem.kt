package com.gultekinahmetabdullah.sirdas.classes.dataclasses

data class ToDoItem(
    val userId: String,
    val id: Int,
    val task: String,
    var isChecked: Boolean = false
) {
    constructor() : this("", 0, "")
}