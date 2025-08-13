package org.example.app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.example.app.adapter.NotesAdapter
import org.example.app.data.NotesRepository
import org.example.app.model.Note

/**
 * PUBLIC_INTERFACE
 * MainActivity is the entry point of the Notes app.
 * It shows a searchable list of notes and a floating action button to create a new note.
 */
class MainActivity : AppCompatActivity(), NotesAdapter.OnNoteClickListener {

    private lateinit var repository: NotesRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesAdapter
    private lateinit var searchView: SearchView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var fab: FloatingActionButton
    private var allNotes: MutableList<Note> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = NotesRepository(this)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = NotesAdapter(allNotes, this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            startActivity(Intent(this, EditNoteActivity::class.java))
        }

        configureSearch()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        allNotes.clear()
        allNotes.addAll(repository.getAllNotes())
        adapter.notifyDataSetChanged()
        toggleEmptyState()
    }

    private fun configureSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterNotes(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterNotes(newText)
                return true
            }
        })
    }

    private fun filterNotes(query: String?) {
        if (TextUtils.isEmpty(query)) {
            adapter.updateData(allNotes)
        } else {
            val results = repository.searchNotes(query!!.trim())
            adapter.updateData(results)
        }
        toggleEmptyState()
    }

    private fun toggleEmptyState() {
        val emptyView: View = findViewById(R.id.emptyView)
        if (adapter.itemCount == 0) {
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    // PUBLIC_INTERFACE
    /** Handles note item clicks to open the note details screen. */
    override fun onNoteClick(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.id)
        startActivity(intent)
    }
}
