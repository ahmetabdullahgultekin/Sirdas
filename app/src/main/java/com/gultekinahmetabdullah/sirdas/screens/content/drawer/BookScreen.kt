package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gultekinahmetabdullah.sirdas.R
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Book
import com.gultekinahmetabdullah.sirdas.viewmodels.BookViewModel

@Composable
fun BookScreen(
    viewModel: BookViewModel
) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var publishedDate by remember { mutableStateOf("") }
    var totalPages by remember { mutableIntStateOf(0) }
    /*    var rating by remember { mutableStateOf("") }
        var pagesRead by remember { mutableIntStateOf(0) }
        var finished by remember { mutableStateOf(false) }*/


    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filterOptions = listOf("All", "Finished", "Reading", "Not Started")


    val books = viewModel.bookListLiveData.value ?: emptyList()

    var selectedBook by remember { mutableStateOf<Book?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.searchBooks(it)
            },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (selectedBook == null) {
            BookList(
                books = books,
                onBookClick = { selectedBook = it }
            )
        } else {
            BookDetail(
                book = selectedBook!!,
                onBackClick = { selectedBook = null },
                /*onPursueClick = { book ->
                    selectedBook = book.copy(finished = !book.finished)
                }*/
            )
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                modifier = Modifier,
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Add Book", style = MaterialTheme.typography.titleLarge)

                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("Author") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = publishedDate,
                        onValueChange = { publishedDate = it },
                        label = { Text("Published Date") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = totalPages.toString(),
                        onValueChange = {
                            totalPages = it.toIntOrNull() ?: 0
                        },
                        label = { Text("Total Pages") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    /*                        TextField(
                                                value = rating,
                                                onValueChange = { rating = it },
                                                label = { Text("Rating") },
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            TextField(
                                                value = pagesRead,
                                                onValueChange = { pagesRead = it },
                                                label = { Text("Pages Read") },
                                                modifier = Modifier.fillMaxWidth(),
                                                enabled = !finished
                                            )*/

                    /*                        Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Checkbox(
                                                    checked = finished,
                                                    onCheckedChange = {
                                                        finished = it
                                                        if (it) {
                                                            pagesRead = totalPages
                                                        }
                                                    }
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Finished")
                                            }*/

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            // Add the book to Firestore
                            if (title.isNotBlank() && author.isNotBlank()
                                && description.isNotBlank() && publishedDate.isNotBlank()
                                && totalPages != 0
                            ) {
                                val book = Book(
                                    id = 0, // Assign an ID or auto-generate
                                    title = title,
                                    author = author,
                                    description = description,
                                    publishedDate = publishedDate,
                                    totalPages = totalPages
                                )
                                viewModel.addOrUpdateBook(book)
                                Toast.makeText(context, "Book added!", Toast.LENGTH_SHORT)
                                    .show()
                                showDialog = false
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please fill in all required fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}

fun addBook() {
    //TODO Implement add book functionality

}

@Composable
fun BookDetail(
    book: Book,
    onBackClick: () -> Unit,
    /*
        onPursueClick: (Book) -> Unit*/
) {
    Column(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.baseline_book_24),
            contentDescription = "Book Cover",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = book.title, style = MaterialTheme.typography.bodyMedium)
        Text(text = "By ${book.author}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Published: ${book.publishedDate}", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = book.description, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        /*Button(
            onClick = { onPursueClick(book) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = if (book.finished) "Mark as Not Pursued" else "Mark as Pursued")
        }*/
    }
}

@Composable
fun BookList(
    books: List<Book>,
    onBookClick: (Book) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(books) { book ->
            BookItem(book = book, onClick = onBookClick)
        }
    }
}

@Composable
fun BookItem(
    book: Book,
    onClick: (Book) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(book) },
        elevation = CardDefaults.cardElevation()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.baseline_book_24),
                contentDescription = "Book Cover",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = book.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = book.author, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Published: ${book.publishedDate}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

