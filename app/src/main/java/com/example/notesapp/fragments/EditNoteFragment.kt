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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xute.markdeditor.EditorControlBar
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle.H1

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

        GlobalScope.launch(Dispatchers.Main) {
            val currentNoteDraft =
                notesViewModel.getDraftContentForEditor(args.selectedNote.note.ID)
            editor.loadDraft(currentNoteDraft)
        }

//        Log.d(
//            TAG, """
//            onCreateView:
//            currentNoteDraft size: ${(currentNoteDraft as DraftModel).items.size}
//        """.trimIndent()
//        )

        editorControlBar.setEditorControlListener(notesViewModel.editorControlListener)
        editor.configureEditor(
            "",               //server url for image upload
            "",            //serverToken
            true,             //isDraft: set true when you are loading draft(fresh editor window)
            "Edit note here",           //default hint of input box
            H1
        )

        Log.d(TAG, "Editor loaded with draft: ${editor.draft}")
        editorControlBar.setEditor(editor)
        return binding.root
    }
}