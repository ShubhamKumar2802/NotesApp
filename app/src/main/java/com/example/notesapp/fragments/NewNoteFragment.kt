package com.example.notesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R
import com.example.notesapp.database.entities.Note
import com.example.notesapp.database.entities.NoteDraftDataItem
import com.example.notesapp.database.entities.NoteDraftModel
import com.example.notesapp.databinding.FragmentNewNoteBinding
import com.example.notesapp.viewModel.NotesViewModel
import xute.markdeditor.EditorControlBar
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle.H1
import xute.markdeditor.datatype.DraftDataItemModel
import xute.markdeditor.utilities.DraftManager

private const val TAG = "NewNoteFragment"

class NewNoteFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var binding: FragmentNewNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")

        // ViewModel
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        // Configure the Editor
        val editor: MarkDEditor = binding.noteTextEditor
        val editorControlBar: EditorControlBar = binding.editorControlBar

        editorControlBar.setEditorControlListener(notesViewModel.editorControlListener)
        editor.configureEditor(
            "",               //server url for image upload
            "",            //serverToken
            true,              //isDraft: set true when you are loading draft(fresh editor window)
            "Type here...",  //default hint of input box
            H1
        )
        editor.loadDraft(notesViewModel.getEditorDraftContent())
        editorControlBar.setEditor(editor)

        binding.buttonConfirmNote.setOnClickListener {
            // editor content in plain text form
            val noteContent = editor.markdownContent
            // editor content in DraftModel form
            val draftContent = DraftManager().processDraftContent(editor)
            // editor content in DraftDataItemModel form
            val draftDataModelItems: ArrayList<DraftDataItemModel> = draftContent.items

            if (draftDataModelItems.isNotEmpty()) {
                Log.d(TAG, "buttonConfirmNote: New note: $noteContent")
                val newNote = Note(0, noteContent, null)
                val newDraftContent = NoteDraftModel(0, newNote.ID)

                draftDataModelItems.forEach {
                    val newDraftDataItem = NoteDraftDataItem(
                        0,
                        newDraftContent.draftID,
                        it.itemType,
                        it.mode,
                        it.style,
                        it.content,
                        it.downloadUrl,
                        it.caption
                    )
                    notesViewModel.insertDraftDataItem(newDraftDataItem)
                }
                notesViewModel.insertNote(newNote, newDraftContent)
                findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)
            }
        }
        return binding.root
    }
}