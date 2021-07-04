package com.example.notesapp.fragments.horizontalRecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.database.entities.entityRelations.NoteWithDraftModel
import com.example.notesapp.databinding.HorizontalRvCardLayoutBinding
import com.example.notesapp.fragments.HomeFragmentDirections
import com.example.notesapp.viewModel.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle
import xute.markdeditor.models.DraftModel

private val TAG = "notesHorizontalAdapter"

class NotesHorizontalAdapter : RecyclerView.Adapter<NotesHorizontalAdapter.NotesViewHolder>() {

    private var notesList = emptyList<NoteWithDraftModel>()

    private lateinit var binding: HorizontalRvCardLayoutBinding
    private lateinit var notesViewModel: NotesViewModel

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        binding = HorizontalRvCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        Log.d(TAG, "onCreateViewHolder: started")
        notesViewModel =
            ViewModelProvider(parent.context as FragmentActivity).get(NotesViewModel::class.java)
        return NotesViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        with(binding) {
            val editor: MarkDEditor = binding.editorHorizontalRv
            editor.configureEditor(
                null,
                null,
                true,
                null,
                TextComponentStyle.NORMAL
            )

//            val currentNoteDraft: DraftModel =
//                notesViewModel.getDraftModel(notesList[position].note.ID)
//            editor.loadDraft(currentNoteDraft)

            GlobalScope.launch(Dispatchers.Main) {
                val currentNoteDraft =
                    notesViewModel.getDraftContentForEditor(notesList[position].note.ID)
                editor.loadDraft(currentNoteDraft)
            }

            cvNotePreview.setOnClickListener {
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

    private suspend fun getCurrentNoteDraftModel(currentCardPosition: Int): DraftModel {
        return notesViewModel.getDraftContentForEditor(notesList[currentCardPosition].note.ID)
    }
}