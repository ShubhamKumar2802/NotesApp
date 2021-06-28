package com.example.notesapp

import androidx.lifecycle.LiveData
import com.example.notesapp.database.NoteDao
import com.example.notesapp.database.entities.Note
import com.example.notesapp.database.entities.NoteDraftDataItem
import com.example.notesapp.database.entities.NoteDraftModel
import com.example.notesapp.database.entities.entityRelations.NoteAndNoteDraftModel
import com.example.notesapp.database.entities.entityRelations.NoteDraftModelWithNoteDraftDataItem

class NotesRepository(private val noteDao: NoteDao) {

    val getNotesList: LiveData<List<NoteAndNoteDraftModel>> = noteDao.getAllNotes()

//    val getDraftModelList: LiveData<List<NoteDraftModelWithNoteDraftDataItem>> = noteDao.getAllDraftModelWithDraftDataItem()

//    fun getNoteAndNoteDraftModel(id: Int): LiveData<List<NoteAndNoteDraftModel>> {
//        return noteDao.getNoteAndDraftModelWithID(id)
//    }

    fun getNoteDraftModelAndNoteDraftDataItem(draftID: Long?): List<NoteDraftModelWithNoteDraftDataItem> {
        return noteDao.getDraftModelWithDraftDataItemWithDraftID(draftID)
    }

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun insertNoteDraftModel(draftModel: NoteDraftModel) {
        noteDao.insertNoteDraftModel(draftModel)
    }

    suspend fun insertNoteDraftDataItem(draftDataItem: NoteDraftDataItem) {
        noteDao.insertNoteDraftDataItem(draftDataItem)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun deleteNoteDraftModel(draftModel: NoteDraftModel) {
        noteDao.deleteNoteDraftModel(draftModel)
    }

    suspend fun deleteNoteDraftDataItem(draftDataItem: NoteDraftDataItem) {
        noteDao.deleteNoteDraftDataItem(draftDataItem)
    }

//    suspend fun getAllNotes() : List<Note> {
//        return noteDao.getAllNotes()
//    }
//
//    suspend fun getNoteWithDraftModelUsingId(id: Int): List<NoteWithDraftModel> {
//        return noteDao.getNoteWithDraftModelUsingId(id)
//    }
}