package com.example.notesapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.notesapp.NotesRepository
import com.example.notesapp.database.Note
import com.example.notesapp.database.NoteDatabase

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    val notesDao = NoteDatabase.getNoteDatabase(application).getNoteDao()
    val notesList: LiveData<List<Note>> = NotesRepository(notesDao).getNotesList

}