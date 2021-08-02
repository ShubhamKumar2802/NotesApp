package com.example.notesapp.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.R
import com.example.notesapp.database.entities.entityRelations.NoteWithDraftModel
import com.example.notesapp.databinding.RvLayoutVerticalBinding

private val TAG = "RecyclerViewAdapter"

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.NotesViewHolder>() {

    private var notesList = emptyList<NoteWithDraftModel>()
    private lateinit var binding: RvLayoutVerticalBinding

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        binding = RvLayoutVerticalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        Log.d(TAG, "onCreateViewHolder: started")
        return NotesViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.NotesViewHolder, position: Int) {
        binding.tvNoteTitleVerticalRv.text = notesList[position].draftModel.first().content

        val bannerImageUrl = notesList[position].note.noteImageBannerURL
        if (bannerImageUrl.isNullOrEmpty()) {
            Log.d(TAG, "onBindViewHolder: empty URL")
        } else {
            Glide.with(holder.itemView)
                .load(notesList[position].note.noteImageBannerURL)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.ivBannerImageVerticalRv)
        }

        binding.cardPreview.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(notesList[position])
            holder.itemView.findNavController().navigate(action)
            Log.d(
                TAG,
                "onBindViewHolder: itemClicked with position: ${notesList[position].note.ID}"
            )
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setData(note: List<NoteWithDraftModel>) {
        Log.d(TAG, "setData: List updated to ${note.size} Notes")
        this.notesList = note
        notifyDataSetChanged()
    }
}