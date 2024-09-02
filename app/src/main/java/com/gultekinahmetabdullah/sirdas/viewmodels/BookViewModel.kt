package com.gultekinahmetabdullah.sirdas.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Book
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val booksCollection = db.collection("books")

    private val _bookLiveData = MutableLiveData<Book?>()
    val bookLiveData: LiveData<Book?> get() = _bookLiveData

    private val _bookListLiveData = MutableLiveData<List<Book>>()
    val bookListLiveData: LiveData<List<Book>> get() = _bookListLiveData

    init {
        try {
            fetchAllBooks()
        } catch (e: Exception) {
            _bookListLiveData.value = emptyList()
            e.printStackTrace()
        }
    }

    // Fetch a single book by ID
    fun fetchBookById(id: Int) {
        viewModelScope.launch {
            booksCollection.whereEqualTo("id", id)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        _bookLiveData.value = documents.first().toObject(Book::class.java)
                    } else {
                        _bookLiveData.value = null
                    }
                }
                .addOnFailureListener {
                    _bookLiveData.value = null
                }
        }
    }

    // Fetch all books
    fun fetchAllBooks() {
        viewModelScope.launch {
            booksCollection.get()
                .addOnSuccessListener { documents ->
                    val bookList = documents.map { it.toObject(Book::class.java) }
                    _bookListLiveData.value = bookList
                }
                .addOnFailureListener {
                    _bookListLiveData.value = emptyList()
                }
        }
    }

    // Add or update a book
    fun addOrUpdateBook(book: Book) {
        viewModelScope.launch {
            booksCollection.document(book.id.toString())
                .set(book)
                .addOnSuccessListener {
                    // Successfully added or updated book
                    fetchAllBooks()
                }
                .addOnFailureListener {
                    // Handle failure

                }
        }
    }

    // Delete a book
    fun deleteBook(id: Int) {
        viewModelScope.launch {
            booksCollection.document(id.toString())
                .delete()
                .addOnSuccessListener {
                    // Successfully deleted book
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }

    // Update specific fields in a book
    fun updateBookFields(id: Int, updates: Map<String, Any>) {
        viewModelScope.launch {
            booksCollection.document(id.toString())
                .update(updates)
                .addOnSuccessListener {
                    // Successfully updated book fields
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }

    // Search books by title or author
    fun searchBooks(query: String) {
        viewModelScope.launch {
            booksCollection
                .orderBy("title")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .addOnSuccessListener { documents ->
                    val bookList = documents.map { it.toObject(Book::class.java) }
                    _bookListLiveData.value = bookList
                }
                .addOnFailureListener {
                    _bookListLiveData.value = emptyList()
                }
        }
    }

    // Filter books based on criteria (e.g., isFinished, rating)
    fun filterBooks(isFinished: Boolean? = null, minRating: Float? = null) {
        viewModelScope.launch {
            var query: Query = booksCollection

            if (isFinished != null) {
                query = query.whereEqualTo("isFinished", isFinished)
            }

            if (minRating != null) {
                query = query.whereGreaterThanOrEqualTo("rating", minRating)
            }

            query.get()
                .addOnSuccessListener { documents ->
                    val bookList = documents.map { it.toObject(Book::class.java) }
                    _bookListLiveData.value = bookList
                }
                .addOnFailureListener {
                    _bookListLiveData.value = emptyList()
                }
        }
    }
}
