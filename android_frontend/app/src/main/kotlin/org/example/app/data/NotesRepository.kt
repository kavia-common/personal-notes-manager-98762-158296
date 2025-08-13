package org.example.app.data

import android.content.Context
import org.example.app.model.Note

/**
 * PUBLIC_INTERFACE
 * NotesRepository provides a simple API to perform CRUD operations on notes data.
 * It encapsulates the database helper and exposes clean methods for UI layers.
 */
class NotesRepository(context: Context) {

    private val db = NotesDatabaseHelper(context.applicationContext)

    // PUBLIC_INTERFACE
    /** Creates a new note with the provided title and content. Throws IllegalArgumentException on invalid input. */
    fun createNote(title: String, content: String): Note {
        require(title.isNotBlank()) { "Title cannot be empty" }
        val now = System.currentTimeMillis()
        return db.insertNote(title.trim(), content.trim(), now)
    }

    // PUBLIC_INTERFACE
    /** Updates the note's title and content. Returns true if the update succeeded. */
    fun updateNote(id: Long, title: String, content: String): Boolean {
        require(title.isNotBlank()) { "Title cannot be empty" }
        val now = System.currentTimeMillis()
        return db.updateNote(id, title.trim(), content.trim(), now)
    }

    // PUBLIC_INTERFACE
    /** Deletes the note by ID. Returns true if a note was removed. */
    fun deleteNote(id: Long): Boolean = db.deleteNote(id)

    // PUBLIC_INTERFACE
    /** Retrieves a note by ID, or null if not found. */
    fun getNoteById(id: Long): Note? = db.getNoteById(id)

    // PUBLIC_INTERFACE
    /** Retrieves all notes ordered by last updated time descending. */
    fun getAllNotes(): List<Note> = db.getAllNotes()

    // PUBLIC_INTERFACE
    /** Searches notes by title or content. */
    fun searchNotes(query: String): List<Note> = db.searchNotes(query)
}
