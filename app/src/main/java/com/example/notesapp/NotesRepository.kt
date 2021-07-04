package com.example.notesapp

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.notesapp.database.NoteDao
import com.example.notesapp.database.entities.DraftModel
import com.example.notesapp.database.entities.Note
import com.example.notesapp.database.entities.entityRelations.NoteWithDraftModel

private val TAG = "NotesRepository"

class NotesRepository(private val noteDao: NoteDao) {

    val allNotesList: LiveData<List<NoteWithDraftModel>> = noteDao.getAllNotes()

    suspend fun getNoteWithDraftModelUsingId(id: Long): List<NoteWithDraftModel> {
        return noteDao.getNoteWithDraftModelUsingId(id)
    }

    suspend fun getNoteIdUsingNoteText(noteText: String): Long {
        Log.d(
            TAG,
            "getNoteIdUsingNoteText: returning note ID = ${noteDao.getNoteIdUsingNoteText(noteText)}"
        )
        return noteDao.getNoteIdUsingNoteText(noteText)
    }

    suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    suspend fun addDraftModel(draftModel: DraftModel) {
        noteDao.addDraftModel(draftModel)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun deleteDraftModel(draftModel: DraftModel) {
        noteDao.deleteDraftModel(draftModel)
    }
}