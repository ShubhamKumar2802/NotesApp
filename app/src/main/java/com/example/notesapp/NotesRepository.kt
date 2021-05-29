package com.example.notesapp

import androidx.lifecycle.LiveData
import com.example.notesapp.database.Note
import com.example.notesapp.database.NoteDao

class NotesRepository(private val noteDao: NoteDao) {

    val getNotesList: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}