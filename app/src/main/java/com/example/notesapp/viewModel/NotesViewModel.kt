package com.example.notesapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.NotesRepository
import com.example.notesapp.database.Note
import com.example.notesapp.database.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "NotesViewModel"

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    val notesList: LiveData<List<Note>>
    private val notesRepository: NotesRepository


    init {
        val notesDao = NoteDatabase.getNoteDatabase(application).getNoteDao()
        notesRepository = NotesRepository(notesDao)
        notesList = notesRepository.getNotesList
    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.insertNote(note)
            Log.d(TAG, "Note Added: ID:${note.ID}; Content:${note.noteText}")
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteNote(note)
            Log.d(TAG, "Note deleted: $note")
        }
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return notesRepository.getNotesList
    }

}