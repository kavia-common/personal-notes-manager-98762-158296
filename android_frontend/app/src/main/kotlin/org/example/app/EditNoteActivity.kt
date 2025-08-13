package org.example.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import org.example.app.data.NotesRepository
import org.example.app.model.Note

/**
 * PUBLIC_INTERFACE
 * EditNoteActivity allows creating a new note or editing an existing one.
 * It uses a toolbar action to save changes.
 */
class EditNoteActivity : AppCompatActivity() {

    private lateinit var repository: NotesRepository
    private var editingNoteId: Long? = null

    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        repository = NotesRepository(this)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        titleInput = findViewById(R.id.titleInput)
        contentInput = findViewById(R.id.contentInput)

        val noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1)
        if (noteId > 0) {
            val note: Note? = repository.getNoteById(noteId)
            if (note != null) {
                editingNoteId = note.id
                title = getString(R.string.edit_note)
                titleInput.setText(note.title)
                contentInput.setText(note.content)
            }
        } else {
            title = getString(R.string.new_note)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { finish(); true }
            R.id.action_save -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val titleText = titleInput.text?.toString()?.trim().orEmpty()
        val contentText = contentInput.text?.toString()?.trim().orEmpty()
        if (titleText.isBlank()) {
            Toast.makeText(this, getString(R.string.title_required), Toast.LENGTH_SHORT).show()
            return
        }
        try {
            if (editingNoteId == null) {
                repository.createNote(titleText, contentText)
            } else {
                repository.updateNote(editingNoteId!!, titleText, contentText)
            }
            finish()
        } catch (ex: IllegalArgumentException) {
            Toast.makeText(this, ex.message ?: "Validation error", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }
}
