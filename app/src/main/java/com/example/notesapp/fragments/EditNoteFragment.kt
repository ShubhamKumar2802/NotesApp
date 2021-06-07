package com.example.notesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.notesapp.databinding.FragmentEditNoteBinding
import com.example.notesapp.viewModel.NotesViewModel
import xute.markdeditor.EditorControlBar
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle

class EditNoteFragment : Fragment() {

    private val args by navArgs<EditNoteFragmentArgs>()
    private lateinit var binding: FragmentEditNoteBinding
    private lateinit var notesViewModel: NotesViewModel
    private val TAG = "EditNoteFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")

        // ViewModel
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        // Configure the Editor
        val editor: MarkDEditor = binding.noteEditNoteFragTextEditor
        val editorControlBar: EditorControlBar = binding.editNoteFragControlBar

        editorControlBar.setEditorControlListener(notesViewModel.editorControlListener)
//        val newEditor = MarkDEditor(this.requireContext(), null)
        editor.configureEditor(
            "",               //server url for image upload
            "",            //serverToken
            false,             // isDraft: set true when you are loading draft(fresh editor window)
            null,  //default hint of input box
            TextComponentStyle.H1
        )
        editorControlBar.setEditor(editor)
        return binding.root
    }
}