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
import xute.markdeditor.EditorControlBar
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle.H1

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
            true,              // isDraft: set true when you are loading draft(fresh editor window)
            "Type here...",  //default hint of input box
            H1
        )
        editor.loadDraft(notesViewModel.getEditorDraftContent())
        editorControlBar.setEditor(editor)

        return binding.root
    }
}