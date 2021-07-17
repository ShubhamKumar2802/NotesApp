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

    suspend fun getNoteUsingId(id: Long): Note {
        return noteDao.getNoteUsingId(id)
    }

    suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    suspend fun addDraftModel(draftModel: DraftModel) {
        noteDao.addDraftModel(draftModel)
    }

    //    suspend fun deleteNote(id: Long) {
//        noteDao.deleteNote(id)
//    }
//
//    suspend fun deleteDraftModel(id: Long) {
//        noteDao.deleteDraftModel(id)
//    }
    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun deleteDraftModel(draftModel: DraftModel) {
        noteDao.deleteDraftModel(draftModel)
    }

    suspend fun updateNote(id: Long, noteText: String, bannerImageUrl: String?) {
        noteDao.updateNote(id, noteText, bannerImageUrl)
    }

    suspend fun updateDraftModel(
        id: Long,
        itemType: Int,
        mode: Int,
        style: Int,
        content: String,
        imageUrl: String?,
        caption: String?
    ) {
        noteDao.updateDraftModel(
            id,
            itemType,
            mode,
            style,
            content,
            imageUrl,
            caption
        )
    }
}