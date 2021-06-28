package com.example.notesapp.fragments.horizontalRecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.database.entities.entityRelations.NoteAndNoteDraftModel
import com.example.notesapp.databinding.HorizontalRvCardLayoutBinding
import com.example.notesapp.fragments.HomeFragmentDirections
import com.example.notesapp.viewModel.NotesViewModel
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle

private val TAG = "notesHorizontalAdapter"

class NotesHorizontalAdapter : RecyclerView.Adapter<NotesHorizontalAdapter.NotesViewHolder>() {

    private var notesList = emptyList<NoteAndNoteDraftModel>()

    //    private var draftModelWithDraftDataItemList = emptyList<NoteDraftModelWithNoteDraftDataItem>()
//    private var draftModelList = emptyList<NoteDraftModel>()
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
            val currentNoteDraft =
                notesViewModel.getDraftFromDatabase(notesList[position].noteDraftModel?.draftID)
            val editor: MarkDEditor = binding.editorHorizontalRv
//            val editorControlBar: EditorControlBar = binding.editorControlBar
//            editorControlBar.setEditorControlListener(notesViewModel.editorControlListener)
            editor.configureEditor(
                null,
                null,
                true,
                null,
                TextComponentStyle.NORMAL
            )
            editor.loadDraft(currentNoteDraft)
//            editorControlBar.setEditor(editor)
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

    fun setData(note: List<NoteAndNoteDraftModel>) {
        this.notesList = note
        notifyDataSetChanged()
        Log.d(TAG, "setData: List updated to $itemCount Notes")
    }

//    fun setDraftData(draftModelWithNoteDraftDataItem: List<NoteDraftModelWithNoteDraftDataItem>) {
//        this.draftModelWithDraftDataItemList = draftModelWithNoteDraftDataItem
//        notifyDataSetChanged()
//        Log.d(TAG, "setDraftData: DraftLst updated")
//    }
}