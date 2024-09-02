package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.sirdas.R
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Directory
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Document
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.FileObject
import com.gultekinahmetabdullah.sirdas.viewmodels.DirectoryViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.DocumentViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(viewModel: DocumentViewModel, directoryViewModel: DirectoryViewModel) {

    val fileObjects = remember {
        mutableListOf(
            Document(
                uid = "",
                id = "",
                name = "File 1",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                directory = false,
                path = "",
                downloadUrl = ""
            ),
            Document(
                uid = "",
                id = "",
                name = "File 2",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                directory = false,
                path = "",
                downloadUrl = ""
            ),
            Directory(
                id = "",
                name = "Directory 1",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            Directory(
                id = "",
                name = "Directory 2",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            Document(
                uid = "",
                id = "",
                name = "File 3",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                directory = false,
                path = "",
                downloadUrl = ""
            ),
            Document(
                uid = "",
                id = "",
                name = "File 4",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                directory = false,
                path = "",
                downloadUrl = ""
            ),
            Directory(
                id = "",
                name = "Directory 3",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            Directory(
                id = "",
                name = "Directory 4",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            Document(
                uid = "",
                id = "",
                name = "File 5",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                directory = false,
                path = "",
                downloadUrl = ""
            ),
            Document(
                uid = "",
                id = "",
                name = "File 6",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                directory = false,
                path = "",
                downloadUrl = ""
            ),
            Directory(
                id = "",
                name = "Directory 5",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            Directory(
                id = "",
                name = "Directory 6",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            Document(
                uid = "",
                id = "",
                name = "File 7",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                directory = false,
                path = "",
                downloadUrl = ""
            ),
            Document(
                uid = "",
                id = "",
                name = "File 8",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                directory = false,
                path = "",
                downloadUrl = ""
            ),
            Directory(
                id = "",
                name = "Directory 7",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            Directory(
                id = "",
                name = "Directory 8",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            Document(
                uid = "",
                id = "",
                name = "File 9",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                directory = false,
                path = "",
                downloadUrl = ""
            ),
            Document(
                uid = "",
                id = "",
                name = "File 10",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                directory = false,
                path = "",
                downloadUrl = ""
            ),
            Directory(
                id = "",
                name = "Directory 9",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            Directory(
                id = "",
                name = "Directory 10",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
        )
    }


    val context = LocalContext.current
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("new") }
    var renameFileItem by remember { mutableStateOf<Document?>(null) }
    var newFileName by remember { mutableStateOf("") }

    // Register file picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri
        fileName = context.getFileName(uri!!).toString()
    }

    //val directories by directoryViewModel.fetchDirectories().observeAsState(emptyList())


    // sort directories and files
    fileObjects.sortedBy { it.directory }

    var showAddDialog by remember { mutableStateOf(false) }
    var currentDirectory by remember { mutableStateOf(Directory()) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0f),
                title = { Text("Documents") }
            )
        },
        floatingActionButton = {
//            FloatingActionButton(onClick = { filePickerLauncher.launch("*/*") }) {
//                Icon(Icons.Default.Add, contentDescription = "Upload File")
//            }
            FloatingActionButton(onClick = {
                currentDirectory = Directory() // Reset for a new directory
                showAddDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Directory")
            }
        },
        bottomBar = {
            // BottomBar
            BottomAppBar(
                modifier = Modifier.height(0.dp)
            ) {
                // BottomBar content
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Display directories
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                items(fileObjects.sortedBy {
                    !it.directory
                }) { fileObject ->
                    FileObjectItem(fileObject, onEdit = {
                        currentDirectory = it as Directory
                        showAddDialog = true
                    }, onDelete = {
                        directoryViewModel.deleteDirectory(it.id)
                    })
                }
            }

            if (showAddDialog) {
                AddEditDirectoryDialog(
                    directory = currentDirectory,
                    onDismiss = { showAddDialog = false },
                    onSave = { fileObject ->
                        directoryViewModel.addOrUpdateDirectory(fileObject)
                        showAddDialog = false
                    }
                )
            }

            // File upload section
            /* if (selectedUri != null) {
                 Column {
                     BasicTextField(
                         value = fileName,
                         onValueChange = { fileName = it },
                         modifier = Modifier
                             .fillMaxWidth()
                             .border(
                                 1.dp,
                                 MaterialTheme.colorScheme.onSurface,
                                 MaterialTheme.shapes.small
                             ),
                         textStyle = TextStyle(MaterialTheme.colorScheme.onSurface),
                         decorationBox = { innerTextField ->
                             Box(
                                 modifier = Modifier.padding(8.dp)
                             ) {
                                 if (fileName.isEmpty()) Text("Enter file name")
                                 innerTextField()
                             }
                         }
                     )
                     Button(
                         onClick = {
                             selectedUri?.let {
                                 viewModel.uploadFile(it, fileName)
                                 fileName = ""
                                 selectedUri = null
                             }
                         },
                         modifier = Modifier.padding(top = 8.dp)
                     ) {
                         Text("Upload")
                     }
                 }
             }

             Spacer(modifier = Modifier.height(16.dp))

             if (viewModel.fileItems.isEmpty()) {
                 CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
             }

             // List of files
             LazyColumn(
                 verticalArrangement = Arrangement.spacedBy(8.dp),
                 modifier = Modifier.fillMaxHeight()
             ) {

                 items(viewModel.fileItems.size) { index ->
                     FileItemRow(fileItem = viewModel.fileItems[index],
                         onDownload = {
                             viewModel.downloadFile(
                                 context = context,
                                 fileName = viewModel.fileItems[index].name,
                                 downloadUrl = viewModel.fileItems[index].downloadUrl
                             )
                         },
                         onRename = {
                             renameFileItem = viewModel.fileItems[index]
                             newFileName = viewModel.fileItems[index].name
                         },
                         onDelete = {
                             viewModel.deleteFile(viewModel.fileItems[index])
                         })
                 }
             }*/
        }

        // Rename dialog
        if (renameFileItem != null) {
            AlertDialog(
                onDismissRequest = { renameFileItem = null },
                title = { Text("Rename File") },
                text = {
                    Column {
                        Text("Enter new name for the file:")
                        TextField(
                            value = newFileName,
                            onValueChange = { newFileName = it },
                            placeholder = { Text("New file name") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        renameFileItem?.let {
                            viewModel.updateFileName(context, it, newFileName)
                            renameFileItem = null
                            newFileName = ""
                        }
                    }) {
                        Text("Rename")
                    }
                },
                dismissButton = {
                    Button(onClick = { renameFileItem = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun FileItemRow(
    fileItem: Document, onDownload: () -> Unit, onRename: () -> Unit, onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = fileItem.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { onDownload() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_file_download_24),
                contentDescription =
                "Download"
            )
        }

        IconButton(onClick = { onRename() }) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Rename"
            )
        }

        IconButton(onClick = { onDelete() }) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


fun Context.getFileName(uri: Uri): String? = when (uri.scheme) {
    ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
    else -> uri.path?.let(::File)?.name
}

private fun Context.getContentFileName(uri: Uri): String? = runCatching {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        cursor.moveToFirst()
        return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
    }
}.getOrNull()


@Composable
fun FileObjectItem(
    fileObject: FileObject,
    onEdit: (FileObject) -> Unit,
    onDelete: (FileObject) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            // Navigate to the directory

        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp),
        ) {
            if (fileObject.directory) {
                Icon(
                    ImageVector.vectorResource(
                        id = R.drawable.baseline_folder_24
                    ),
                    contentDescription = "Folder",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
            } else {
                Icon(
                    ImageVector.vectorResource(
                        id = R.drawable.baseline_insert_drive_file_24
                    ),
                    contentDescription = "File",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Text(fileObject.name, modifier = Modifier.weight(1f))
            IconButton(onClick = { onEdit(fileObject) }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { onDelete(fileObject) }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddEditDirectoryDialog(
    directory: Directory,
    onDismiss: () -> Unit,
    onSave: (FileObject) -> Unit
) {
    var name by remember { mutableStateOf(directory.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (directory.id.isEmpty()) "Add Directory" else "Edit Directory") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Directory Name") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(directory.copy(name = name, updatedAt = System.currentTimeMillis()))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}