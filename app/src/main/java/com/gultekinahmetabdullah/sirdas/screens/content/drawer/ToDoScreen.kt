package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gultekinahmetabdullah.sirdas.dataclasses.ToDoItem
import com.gultekinahmetabdullah.sirdas.viewmodels.ToDoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(viewModel: ToDoViewModel = viewModel()) {
    var newTask by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        viewModel.fetchTodos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.addToDoItem(newTask)
                newTask = "" // Reset input field after adding the task
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TextField(
                value = newTask,
                onValueChange = { newTask = it },
                label = { Text("New Task") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(viewModel.toDoItems.size) { index ->
                    ToDoItemRow(
                        item = viewModel.toDoItems[index],
                        onToggleCheck = { viewModel.toggleCheck(viewModel.toDoItems[index]) },
                        onDelete = { viewModel.deleteToDoItem(viewModel.toDoItems[index]) }
                    )
                }
            }
        }
    }
}

@Composable
fun ToDoItemRow(item: ToDoItem, onToggleCheck: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = { onToggleCheck() }
        )

        Text(
            text = item.task,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )

        IconButton(onClick = { onDelete() }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Task")
        }
    }
}
