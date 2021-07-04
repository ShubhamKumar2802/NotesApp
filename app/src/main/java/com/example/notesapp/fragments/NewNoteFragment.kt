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
import com.example.notesapp.database.entities.DraftModel
import com.example.notesapp.database.entities.Note
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
            false,              //isDraft: set true when you are loading draft(fresh editor window)
            "Type here...",  //default hint of input box
            H1                       //default heading type
        )

        editorControlBar.setEditor(editor)

        binding.buttonConfirmNote.setOnClickListener {
            val noteText = editor.markdownContent
            val draftDataItemsList = DraftManager().processDraftContent(editor).items
            Log.d(TAG, "onCreateView: draftContentItems: $draftDataItemsList")
            saveNote(
                noteText = noteText,
                bannerImageURL = null,
                draftDataItemsList = draftDataItemsList
            )
            findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)
        }
        return binding.root
    }

    private fun saveNote(
        noteText: String,
        bannerImageURL: String?,
        draftDataItemsList: ArrayList<DraftDataItemModel>
    ) {
        if (noteText.isNotEmpty()) {
            val newNote = Note(
                ID = 0,
                noteText = noteText,
                noteImageBannerURL = bannerImageURL
            )
            notesViewModel.addNewNote(newNote)

            // get the ID for the current note that was just saved in notes_table
            var newNoteId: Long? = null
            try {
                newNoteId = notesViewModel.getNoteIdUsingNoteText(noteText)
                Log.d(TAG, "saveNote: newNoteID received from DB with ID = $newNoteId")
            } catch (exception: Exception) {
                Log.d(TAG, "saveNote: $exception")
            }

            draftDataItemsList.forEach {
                val newDraftModelItem = DraftModel(
                    draftID = 0,
                    ID = newNoteId,
                    itemType = it.itemType,
                    mode = it.mode,
                    style = it.style,
                    content = it.content,
                    imageDownloadURL = it.downloadUrl,
                    imageCaption = it.caption
                )
                notesViewModel.addDraftModel(newDraftModelItem)
            }
        }
    }
}