package org.example.app.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.example.app.model.Note

/**
 * PUBLIC_INTERFACE
 * NotesDatabaseHelper manages the local SQLite database for storing and retrieving notes.
 * This class provides low-level CRUD operations and table/column constants.
 */
class NotesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_NOTES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT NOT NULL,
                $COL_CONTENT TEXT NOT NULL,
                $COL_CREATED_AT INTEGER NOT NULL,
                $COL_UPDATED_AT INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_notes_updated_at ON $TABLE_NOTES($COL_UPDATED_AT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // For simplicity, drop and recreate. In production, use proper migrations.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    // PUBLIC_INTERFACE
    /** Inserts a new note and returns the created Note with generated ID. */
    fun insertNote(title: String, content: String, nowMillis: Long): Note {
        val values = ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_CONTENT, content)
            put(COL_CREATED_AT, nowMillis)
            put(COL_UPDATED_AT, nowMillis)
        }
        val id = writableDatabase.insert(TABLE_NOTES, null, values)
        return Note(id = id, title = title, content = content, createdAt = nowMillis, updatedAt = nowMillis)
    }

    // PUBLIC_INTERFACE
    /** Updates an existing note by ID. Returns true if at least one row updated. */
    fun updateNote(id: Long, title: String, content: String, nowMillis: Long): Boolean {
        val values = ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_CONTENT, content)
            put(COL_UPDATED_AT, nowMillis)
        }
        val rows = writableDatabase.update(
            TABLE_NOTES,
            values,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
        return rows > 0
    }

    // PUBLIC_INTERFACE
    /** Deletes a note by ID. Returns true if a row was deleted. */
    fun deleteNote(id: Long): Boolean {
        val rows = writableDatabase.delete(TABLE_NOTES, "$COL_ID = ?", arrayOf(id.toString()))
        return rows > 0
    }

    // PUBLIC_INTERFACE
    /** Retrieves a note by ID, or null if not found. */
    fun getNoteById(id: Long): Note? {
        val cursor = readableDatabase.query(
            TABLE_NOTES,
            ALL_COLUMNS,
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        cursor.use {
            return if (it.moveToFirst()) readNote(it) else null
        }
    }

    // PUBLIC_INTERFACE
    /** Retrieves all notes ordered by last updated time descending. */
    fun getAllNotes(): List<Note> = queryNotes(null)

    // PUBLIC_INTERFACE
    /** Searches notes by title or content containing the query string. */
    fun searchNotes(q: String): List<Note> {
        val like = "%$q%"
        return queryNotes("$COL_TITLE LIKE ? OR $COL_CONTENT LIKE ?", arrayOf(like, like))
    }

    private fun queryNotes(selection: String?, selectionArgs: Array<String>? = null): List<Note> {
        val list = mutableListOf<Note>()
        val cursor = readableDatabase.query(
            TABLE_NOTES,
            ALL_COLUMNS,
            selection,
            selectionArgs,
            null,
            null,
            "$COL_UPDATED_AT DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(readNote(it))
            }
        }
        return list
    }

    private fun readNote(cursor: Cursor): Note {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT))
        val createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT))
        val updatedAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_UPDATED_AT))
        return Note(id, title, content, createdAt, updatedAt)
    }

    companion object {
        const val DB_NAME = "notes.db"
        const val DB_VERSION = 1

        const val TABLE_NOTES = "notes"
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_CONTENT = "content"
        const val COL_CREATED_AT = "created_at"
        const val COL_UPDATED_AT = "updated_at"

        val ALL_COLUMNS = arrayOf(COL_ID, COL_TITLE, COL_CONTENT, COL_CREATED_AT, COL_UPDATED_AT)
    }
}
