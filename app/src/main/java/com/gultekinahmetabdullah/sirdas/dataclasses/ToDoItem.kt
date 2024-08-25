package com.gultekinahmetabdullah.sirdas.dataclasses

data class ToDoItem(
    val id: Int,
    val task: String,
    var isChecked: Boolean = false
) {
    constructor() : this(0, "", false)
}