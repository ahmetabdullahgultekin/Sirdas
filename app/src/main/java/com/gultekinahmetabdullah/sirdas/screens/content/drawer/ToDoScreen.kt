package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.ToDoItem
import com.gultekinahmetabdullah.sirdas.viewmodels.ToDoViewModel

@Composable
fun ToDoScreen(viewModel: ToDoViewModel = viewModel()) {
    var newTask by remember { mutableStateOf("") }
    var isTextFieldAvailable by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.fetchTodos()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {

            if (isTextFieldAvailable) {
                TextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    label = { Text("New Task") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Row {

            }

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

        FloatingActionButton(
            onClick = {
                if (isTextFieldAvailable) {
                    viewModel.addToDoItem(newTask)
                    newTask = "" // Reset input field after adding the task
                } else {
                    isTextFieldAvailable = true
                }
            }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
                .imePadding()
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
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
