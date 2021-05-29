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
import com.example.notesapp.database.Note
import com.example.notesapp.databinding.FragmentNewNoteBinding
import com.example.notesapp.viewModel.NotesViewModel

private const val TAG = "NewNoteFragment"

class NewNoteFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val newNoteBinding: FragmentNewNoteBinding =
            FragmentNewNoteBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")

        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        newNoteBinding.buttonConfirmNote.setOnClickListener {
            val noteContent = newNoteBinding.etNoteContent.toString()
            if (noteContent.isNotBlank()) {
                Log.d(TAG, "buttonConfirmNote: New note: $noteContent")
                val newNote = Note(0, noteContent)
                notesViewModel.insertNote(newNote)
                findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)
            }
        }
        return newNoteBinding.root
    }
}