package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Category
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.CategoryItem
import com.gultekinahmetabdullah.sirdas.viewmodels.HealthViewModel
import java.util.Date

@Composable
fun HealthScreen(viewModel: HealthViewModel) {
    val categories by remember { mutableStateOf(viewModel.categories) }
    var newCategoryName by remember { mutableStateOf("") }


    Column(modifier = Modifier.padding(16.dp)) {
        // Section to add a new category
        TextField(
            value = newCategoryName,
            onValueChange = { newCategoryName = it },
            label = { Text("New Category Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            if (newCategoryName.isNotBlank()) {
                viewModel.addCategory(newCategoryName)
                newCategoryName = ""
            }
        }) {
            Text("Add Category")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(categories) { category ->
                CategoryCard(category, viewModel)
            }
        }
    }
}

@Composable
fun CategoryCard(category: Category, viewModel: HealthViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemValue by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DetailInfoDialog(item = CategoryItem(
            id = category.id,
            name = category.name,
            value = category.items.size.toString() + " items",
            createdTime = category.createdTime,
            lastEditedTime = category.lastEditedTime
        ), onDismiss = { showDialog = false })
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(8.dp)
                        .clickable {
                            showDialog = true
                        }
                ) {
                    Text(text = category.name, style = MaterialTheme.typography.bodyLarge)
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Expand")
                }
                IconButton(onClick = { viewModel.deleteCategory(category.id) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
                IconButton(onClick = { viewModel.editCategory(category.id, category.name) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
            }

            HorizontalDivider()

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                category.items.forEach { item ->
                    CategoryItemCard(item, category.id, viewModel)
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Section to add a new item to the category
                TextField(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    label = { Text("New Item Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = newItemValue,
                    onValueChange = { newItemValue = it },
                    label = { Text("New Item Value") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(onClick = {
                    if (newItemName.isNotBlank() && newItemValue.isNotBlank()) {
                        viewModel.addItemToCategory(category.id, newItemName, newItemValue)
                        newItemName = ""
                        newItemValue = ""
                    }
                }) {
                    Text("Add Item")
                }
            }
        }
    }
}

@Composable
fun CategoryItemCard(item: CategoryItem, categoryId: String, viewModel: HealthViewModel) {
    var itemName by remember { mutableStateOf(item.name) }
    var itemValue by remember { mutableStateOf(item.value) }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DetailInfoDialog(item = item, onDismiss = { showDialog = false })
    }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = itemName,
                onValueChange = {
                    itemName = it
                    viewModel.editItemInCategory(categoryId, item.id, itemName, itemValue)
                },
                label = { Text("Item Name") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = itemValue,
                onValueChange = {
                    itemValue = it
                    viewModel.editItemInCategory(categoryId, item.id, itemName, itemValue)
                },
                label = { Text("Item Value") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun DetailInfoDialog(
    item: CategoryItem,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Item Details",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(text = "Name: ${item.name}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Value: ${item.value}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Created: ${Date(item.createdTime).toLocaleString()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Last Edited: ${Date(item.lastEditedTime).toLocaleString()}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

