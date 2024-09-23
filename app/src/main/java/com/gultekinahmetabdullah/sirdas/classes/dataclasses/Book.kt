package com.gultekinahmetabdullah.sirdas.classes.dataclasses

data class Book(
    // Unique identifier for the book
    // Auto generated by Firestore
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val publishedYear: Int,
    val totalPages: Int,
) {
    constructor() : this("", "", "", "", 0, 0)
}

/*    val books = listOf(
        Book(
            1,
            "The Great Gatsby",
            "F. Scott Fitzgerald",
            "A novel set in the Jazz Age...",
            "1925",
            180,
        ),
        Book(
            2,
            "To Kill a Mockingbird",
            "Harper Lee",
            "A novel set in the American South...",
            "1960",
            281,
        ),
        Book(
            3,
            "1984",
            "George Orwell",
            "A dystopian novel...",
            "1949",
            328,
        ),
        Book(
            4,
            "Pride and Prejudice",
            "Jane Austen",
            "A novel of manners...",
            "1813",
            226,
        ),
        Book(
            5,
            "The Catcher in the Rye",
            "J.D. Salinger",
            "A novel about a teenager...",
            "1951",
            230,
        ),
        Book(
            6,
            "The Hobbit",
            "J.R.R. Tolkien",
            "A fantasy novel...",
            "1937",
            310,
        )
    )*/

