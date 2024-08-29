package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.sirdas.R
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookPursuingScreen() {
    val books = listOf(
        Book(
            1,
            "The Great Gatsby",
            "F. Scott Fitzgerald",
            "A novel set in the Jazz Age...",
            "1925"
        ),
        Book(
            2,
            "To Kill a Mockingbird",
            "Harper Lee",
            "A novel about the moral growth...",
            "1960"
        ),
        Book(
            3,
            "1984",
            "George Orwell",
            "A dystopian novel...",
            "1949"
        ),
        Book(
            4,
            "Pride and Prejudice",
            "Jane Austen",
            "A novel of manners...",
            "1813"
        ),
        Book(
            5,
            "The Catcher in the Rye",
            "J.D. Salinger",
            "A novel about a teenager...",
            "1951"
        ),
        Book(
            6,
            "The Lord of the Rings",
            "J.R.R. Tolkien",
            "A high fantasy novel...",
            "1954"
        ),
        Book(
            7,
            "The Hobbit",
            "J.R.R. Tolkien",
            "A children's fantasy novel...",
            "1937"
        ),
        Book(
            8,
            "The Little Prince",
            "Antoine de Saint-Exupéry",
            "A novella...",
            "1943"
        ),
        Book(
            9,
            "The Chronicles of Narnia",
            "C.S. Lewis",
            "A series of seven high fantasy novels...",
            "1950-1956"
        ),
        Book(
            10,
            "The Hunger Games",
            "Suzanne Collins",
            "A dystopian novel...",
            "2008"
        )
    )

    var selectedBook by remember { mutableStateOf<Book?>(null) }

    Scaffold(
        topBar = { //TODO Add search functionality
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        floatingActionButton = {
            Column {
                Button(onClick = { addBook() }) {
                    Text(text = "Add Book")
                }
                Button(onClick = { selectedBook = books.random() }) {
                    Text(text = "Random Book")
                }
            }
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (selectedBook == null) {
                BookList(
                    books = books,
                    onBookClick = { selectedBook = it }
                )
            } else {
                BookDetail(
                    book = selectedBook!!,
                    onBackClick = { selectedBook = null },
                    onPursueClick = { book ->
                        selectedBook = book.copy(isPursued = !book.isPursued)
                    }
                )
            }
        }
    }

}

fun addBook() {

}

@Composable
fun BookDetail(
    book: Book,
    onBackClick: () -> Unit,
    onPursueClick: (Book) -> Unit
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

        Button(
            onClick = { onPursueClick(book) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = if (book.isPursued) "Mark as Not Pursued" else "Mark as Pursued")
        }
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

