package com.gultekinahmetabdullah.sirdas.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.ToDoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel : ViewModel() {

    private var nextId = 0
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val db = FirebaseFirestore.getInstance()
    private val todosCollection = db.collection("todos")

    var toDoItems = mutableStateListOf<ToDoItem>()
        private set

    fun addToDoItem(task: String) {
        if (task.isNotBlank()) {
            ++nextId
            //toDoItems.add(ToDoItem(id = nextId, task = task))
            addTodo(ToDoItem(userId, id = nextId, task = task))
        }
    }

    fun deleteToDoItem(item: ToDoItem) {
        //toDoItems.remove(item)
        deleteTodo(item)
    }

    fun toggleCheck(item: ToDoItem) {
//        val index = toDoItems.indexOf(item)
//        if (index != -1) {
//            // Update the item in the list, triggering recomposition
//            //toDoItems[index] = item.copy(isChecked = !item.isChecked)
//        }
        updateTodo(item.copy(isChecked = !item.isChecked))
    }

    fun clearCheckedItems() {
        toDoItems.removeAll { it.isChecked }
    }

    fun clearAllItems() {
        toDoItems.clear()
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        if (fromIndex in toDoItems.indices && toIndex in toDoItems.indices) {
            val item = toDoItems.removeAt(fromIndex)
            toDoItems.add(toIndex, item)
        }
    }

    fun editItem(item: ToDoItem, newTask: String) {
        val index = toDoItems.indexOf(item)
        if (index != -1) {
            toDoItems[index] = item.copy(task = newTask)
        }
    }

    fun copyItem(item: ToDoItem) {
        toDoItems.add(item.copy(id = nextId++))
    }

    // Fetch all to-do items
    fun fetchTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            todosCollection.get().addOnSuccessListener { result ->
                toDoItems.clear()
                for (document in result) {
                    val todoItem = document.toObject(ToDoItem::class.java)
                    toDoItems.add(todoItem)
                }
            }.addOnFailureListener { exception ->
                // Handle error
                Log.w("TodoViewModel", "Error getting documents.", exception)
            }
        }
    }

    // Add a new to-do item
    private fun addTodo(todoItem: ToDoItem) {
        val newTodo =
            ToDoItem(userId, id = todoItem.id, task = todoItem.task, isChecked = todoItem.isChecked)
        viewModelScope.launch(Dispatchers.IO) {
            todosCollection.document(newTodo.id.toString()).set(newTodo).addOnSuccessListener {
                toDoItems.add(newTodo)
            }.addOnFailureListener { exception ->
                // Handle error
                Log.w("TodoViewModel", "Error adding document.", exception)
            }
        }
    }

    // Update an existing to-do item
    private fun updateTodo(todoItem: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todosCollection.document(todoItem.id.toString()).set(todoItem).addOnSuccessListener {
                val index = toDoItems.indexOfFirst { it.id == todoItem.id }
                if (index != -1) {
                    toDoItems[index] = todoItem
                }
            }.addOnFailureListener { exception ->
                // Handle error
                Log.w("TodoViewModel", "Error updating document.", exception)
            }
        }
    }

    // Delete a to-do item
    private fun deleteTodo(todoItem: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todosCollection.document(todoItem.id.toString()).delete().addOnSuccessListener {
                toDoItems.remove(todoItem)
            }.addOnFailureListener { exception ->
                // Handle error
                Log.w("TodoViewModel", "Error deleting document.", exception)
            }
        }
    }
}
