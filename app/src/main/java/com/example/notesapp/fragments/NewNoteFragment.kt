package com.example.notesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.databinding.FragmentNewNoteBinding
import com.example.notesapp.viewModel.NotesViewModel
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle.NORMAL

private const val TAG = "NewNoteFragment"

class NewNoteFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val newNoteBinding: FragmentNewNoteBinding =
            FragmentNewNoteBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")

        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        val editor: MarkDEditor = newNoteBinding.noteTextEditor
        val editorControlBar = newNoteBinding.editorControlBar

        //TODO "Add Controller Listener here"
//        editorControlBar.setEditorControlListener()

        editor.configureEditor(
            "",               //server url for image upload
            "",            //serverToken
            true,              // isDraft: set true when you are loading draft
            "Type here...",  //default hint of input box
            NORMAL
        )
//        editor.loadDraft(notesViewModel.getEditorDraftContent())
        editorControlBar.setEditor(editor)


//        newNoteBinding.buttonConfirmNote.setOnClickListener {
//            val noteContent = newNoteBinding.etNoteContent.text.toString()
//            if (noteContent.isNotBlank()) {
//                Log.d(TAG, "buttonConfirmNote: New note: $noteContent")
//                val newNote = Note(0, noteContent)
//                notesViewModel.insertNote(newNote)
//                findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)
//            }
//        }

        return newNoteBinding.root
    }
}