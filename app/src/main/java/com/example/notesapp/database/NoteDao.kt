package com.example.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notesapp.database.entities.DraftModel
import com.example.notesapp.database.entities.Note
import com.example.notesapp.database.entities.entityRelations.NoteWithDraftModel

@Dao
interface NoteDao {
    @Insert
    suspend fun addNote(note: Note)

    @Insert
    suspend fun addDraftModel(draftModel: DraftModel)

    @Delete
    suspend fun deleteNote(note: Note)

    @Delete
    suspend fun deleteDraftModel(draftModel: DraftModel)

    @Transaction
    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): LiveData<List<NoteWithDraftModel>>

    @Transaction
    @Query("SELECT ID FROM notes_table WHERE noteText = :noteText")
    suspend fun getNoteIdUsingNoteText(noteText: String): Long

    @Transaction
    @Query("SELECT * FROM notes_table WHERE ID = :id")
    suspend fun getNoteWithDraftModelUsingId(id: Long): List<NoteWithDraftModel>
}