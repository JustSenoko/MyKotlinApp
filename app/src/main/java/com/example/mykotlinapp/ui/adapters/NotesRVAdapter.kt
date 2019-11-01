package com.example.mykotlinapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlinapp.R
import com.example.mykotlinapp.data.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NotesRVAdapter(val onItemClick: ((Note) -> Unit)? = null) : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView) {
            tv_title.text = note.title
            tv_text.text = note.text

            val color = when (note.color) {
                Note.Colors.WHITE -> R.color.white
                Note.Colors.YELLOW -> R.color.yellow
                Note.Colors.GREEN -> R.color.green
                Note.Colors.BLUE -> R.color.blue
                Note.Colors.RED -> R.color.red
                Note.Colors.VIOLET -> R.color.violet
            }
            setBackgroundColor(ContextCompat.getColor(itemView.context, color))

            itemView.setOnClickListener {
                onItemClick?.invoke(note)
            }
        }
    }
}