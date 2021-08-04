package com.example.notesapp.database

import androidx.lifecycle.LiveData
import com.example.notesapp.database.entities.Note

private const val TAG = "NotesRepository"

class NotesRepository(private val noteDao: NoteDao) {

    val allNotesList: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun getNoteUsingId(noteId: Long): Note {
        return noteDao.getNoteUsingId(noteId)
    }
}