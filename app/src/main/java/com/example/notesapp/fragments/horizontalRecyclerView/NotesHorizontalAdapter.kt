package com.example.notesapp.fragments.horizontalRecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.database.Note
import com.example.notesapp.databinding.HorizontalRvCardLayoutBinding
import com.example.notesapp.fragments.HomeFragmentDirections
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle

private val TAG = "notesHorizontalAdapter"

class NotesHorizontalAdapter : RecyclerView.Adapter<NotesHorizontalAdapter.NotesViewHolder>() {

    private var notesList = emptyList<Note>()
    private lateinit var binding: HorizontalRvCardLayoutBinding

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        binding = HorizontalRvCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        Log.d(TAG, "onCreateViewHolder: started")
        return NotesViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        with(binding) {
//            tvNoteTitle.text = notesList[position].ID.toString()
//            tvNoteContents.text = notesList[position].noteText
//            ivNoteBannerImage.setImageResource(R.drawable.ic_image_not_loaded)

            // Configure the Editor
            val editor: MarkDEditor = binding.editorHorizontalRv
            editor.configureEditor(
                "",               //server url for image upload
                "",            //serverToken
                true,             // isDraft: set true when you are loading draft(fresh editor window)
                "Note Preview Here",  //default hint of input box
                TextComponentStyle.H1
            )

            cvNotePreview.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(notesList[position])
                holder.itemView.findNavController().navigate(action)
                Log.d(TAG, "onBindViewHolder: itemClicked with position: ${notesList[position].ID}")
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setData(note: List<Note>) {
        this.notesList = note
        notifyDataSetChanged()
    }
}