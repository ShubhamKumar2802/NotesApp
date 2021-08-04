package com.example.notesapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.database.entities.Note
import com.example.notesapp.databinding.RvLayoutVerticalBinding
import com.example.notesapp.ui.fragments.HomeFragmentDirections

private const val TAG = "RecyclerViewAdapter"

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.NotesViewHolder>() {

    private var notesList: List<Note> = ArrayList()
    private lateinit var binding: RvLayoutVerticalBinding

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class NotesDiffCallback(
        var oldNotesList: List<Note>,
        var newNotesList: List<Note>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldNotesList.size
        }

        override fun getNewListSize(): Int {
            return newNotesList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldNotesList[oldItemPosition].ID == newNotesList[newItemPosition].ID)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldNotesList[oldItemPosition].equals(newNotesList[newItemPosition]))
        }
    }

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
        binding.tvNoteTitleVerticalRv.text = notesList[position].noteContents.items.first().content

        val bannerImageUrl = notesList[position].bannerImage?.imageUrl
        if (bannerImageUrl.isNullOrEmpty()) {
            Log.d(TAG, "onBindViewHolder: empty URL")
        } else {
            Glide.with(holder.itemView)
                .load(bannerImageUrl)
//                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.ivBannerImageVerticalRv)
        }

        binding.cardPreview.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(notesList[position].ID)
            holder.itemView.findNavController().navigate(action)
            Log.d(
                TAG,
                "onBindViewHolder: itemClicked with position: ${notesList[position].ID}"
            )
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setData(note: List<Note>) {
        Log.d(TAG, "setData: List updated to ${note.size} Notes")
        val oldNotesList = notesList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            NotesDiffCallback(
                oldNotesList = oldNotesList,
                newNotesList = note
            )
        )
        notesList = note
        diffResult.dispatchUpdatesTo(this)
    }
}