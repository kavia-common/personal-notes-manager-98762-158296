package org.example.app.model

/**
 * PUBLIC_INTERFACE
 * Note represents a note entity stored locally in SQLite.
 */
data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)
