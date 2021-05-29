package com.example.notesapp.fragments.horizontalRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.database.Note
import com.example.notesapp.databinding.HorizontalRvCardLayoutBinding

class notesHorizontalAdapter : RecyclerView.Adapter<notesHorizontalAdapter.notesViewHolder>() {

    private var notesList = emptyList<Note>()
    private lateinit var binding: HorizontalRvCardLayoutBinding

    inner class notesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.tv_note_title)
        val noteContent: TextView = itemView.findViewById(R.id.tv_note_contents)
//        val noteBanner: ImageView = itemView.findViewById(R.id.ivNoteBanner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_rv_card_layout, parent, false)
        return notesViewHolder(view)
    }

    override fun onBindViewHolder(holder: notesViewHolder, position: Int) {
        holder.noteTitle.text = notesList[position].ID.toString()
        holder.noteContent.text = notesList[position].noteText
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setData(note: List<Note>) {
        this.notesList = note
        notifyDataSetChanged()
    }
}