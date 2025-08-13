package org.example.app

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import org.example.app.data.NotesRepository
import org.example.app.model.Note
import org.example.app.util.TimeUtils

/**
 * PUBLIC_INTERFACE
 * NoteDetailActivity shows the content of a note and allows editing or deleting it.
 */
class NoteDetailActivity : AppCompatActivity() {

    private lateinit var repository: NotesRepository
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        repository = NotesRepository(this)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1)
        if (noteId <= 0) {
            finish()
            return
        }

        note = repository.getNoteById(noteId)
        if (note == null) {
            finish()
            return
        }

        supportActionBar?.title = note!!.title

        val content: TextView = findViewById(R.id.content)
        val timestamp: TextView = findViewById(R.id.timestamp)

        content.text = note!!.content
        timestamp.text = getString(R.string.updated_at, TimeUtils.formatRelative(note!!.updatedAt))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { finish(); true }
            R.id.action_edit -> {
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra(EditNoteActivity.EXTRA_NOTE_ID, note!!.id)
                startActivity(intent)
                true
            }
            R.id.action_delete -> {
                confirmDelete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_note)
            .setMessage(R.string.delete_note_confirm)
            .setPositiveButton(R.string.delete) { _: DialogInterface, _: Int ->
                note?.let { repository.deleteNote(it.id) }
                finish()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }
}
