package org.example.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import org.example.app.R
import org.example.app.model.Note
import org.example.app.util.TimeUtils

/**
 * PUBLIC_INTERFACE
 * NotesAdapter renders a list of notes and forwards click events to the provided listener.
 */
class NotesAdapter(
    private var items: MutableList<Note>,
    private val listener: OnNoteClickListener
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    interface OnNoteClickListener {
        // PUBLIC_INTERFACE
        /** Called when a note item is clicked. */
        fun onNoteClick(note: Note)
    }

    fun updateData(newItems: List<Note>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = items[position]
        holder.bind(note)
        holder.itemView.setOnClickListener { listener.onNoteClick(note) }
    }

    override fun getItemCount(): Int = items.size

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container: MaterialCardView = itemView.findViewById(R.id.card)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val snippet: TextView = itemView.findViewById(R.id.snippet)
        private val updatedAt: TextView = itemView.findViewById(R.id.updatedAt)

        fun bind(note: Note) {
            title.text = note.title
            snippet.text = note.content.trim().replace(Regex("\\s+"), " ").take(120)
            updatedAt.text = TimeUtils.formatRelative(note.updatedAt)
            container.isChecked = false
        }
    }
}
