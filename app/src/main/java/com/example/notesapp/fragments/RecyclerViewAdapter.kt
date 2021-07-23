package com.example.notesapp.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notesapp.R
import com.example.notesapp.database.entities.entityRelations.NoteWithDraftModel
import com.example.notesapp.databinding.RvLayoutVerticalBinding
import com.example.notesapp.viewModel.NotesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val TAG = "RecyclerViewAdapter"

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.NotesViewHolder>() {

    private var notesList = emptyList<NoteWithDraftModel>()
    private lateinit var binding: RvLayoutVerticalBinding
    private lateinit var notesViewModel: NotesViewModel

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        binding = RvLayoutVerticalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        Log.d(TAG, "onCreateViewHolder: started")
        notesViewModel =
            ViewModelProvider(parent.context as FragmentActivity).get(NotesViewModel::class.java)
        return NotesViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.NotesViewHolder, position: Int) {
        with(binding) {
            CoroutineScope(Dispatchers.IO).launch {
                val currentNoteDraft =
                    notesViewModel.getDraftContentForEditor(notesList[position].note.ID)
//                editor.loadDraft(currentNoteDraft)
                CoroutineScope(Dispatchers.Main).launch {
                    tvNoteTitleVerticalRv.text = currentNoteDraft.items.first().content.toString()
                }
            }

            val bannerImageUrl = notesList[position].note.noteImageBannerURL
            if (bannerImageUrl.isNullOrEmpty()) {
                Log.d(TAG, "onBindViewHolder: empty URL")
            } else {
                Glide.with(holder.itemView)
                    .load(notesList[position].note.noteImageBannerURL)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(binding.ivBannerImageVerticalRv)
            }

            cardPreview.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(notesList[position])
                holder.itemView.findNavController().navigate(action)
                Log.d(
                    TAG,
                    "onBindViewHolder: itemClicked with position: ${notesList[position].note.ID}"
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setData(note: List<NoteWithDraftModel>) {
        this.notesList = note
        notifyDataSetChanged()
        Log.d(TAG, "setData: List updated to $itemCount Notes")
    }
}