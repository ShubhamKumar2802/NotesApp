package com.example.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notesapp.database.entities.Note
import com.example.notesapp.database.entities.NoteDraftDataItem
import com.example.notesapp.database.entities.NoteDraftModel
import com.example.notesapp.database.entities.entityRelations.NoteAndNoteDraftModel
import com.example.notesapp.database.entities.entityRelations.NoteDraftModelWithNoteDraftDataItem

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNoteDraftModel(draftModel: NoteDraftModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNoteDraftDataItem(draftDataItem: NoteDraftDataItem)

    @Delete
    suspend fun deleteNote(note: Note)

    @Delete
    suspend fun deleteNoteDraftModel(draftModel: NoteDraftModel)

    @Delete
    suspend fun deleteNoteDraftDataItem(draftDataItem: NoteDraftDataItem)

    @Transaction
    @Query("SELECT * FROM notes_table ORDER BY ID")
    fun getAllNotes(): LiveData<List<NoteAndNoteDraftModel>>

//    @Transaction
//    @Query("SELECT * FROM NoteDraftModel ORDER BY ID")
//    fun getAllDraftModelWithDraftDataItem(): LiveData<List<NoteDraftModelWithNoteDraftDataItem>>

    @Transaction
    @Query("SELECT * FROM notes_table WHERE ID = :id")
    fun getNoteAndDraftModelWithID(id: Int): LiveData<List<NoteAndNoteDraftModel>>

    @Transaction
    @Query("SELECT * FROM NoteDraftModel WHERE draftID = :draftID")
    fun getDraftModelWithDraftDataItemWithDraftID(draftID: Long?): List<NoteDraftModelWithNoteDraftDataItem>

    // _-----------------------------------------------------------
//    @Insert
//    suspend fun addNote(note: Note)
//
//    @Insert
//    suspend fun addDraftModel(draftModel: DraftModel)
//
//    @Delete
//    suspend fun deleteNote(note: Note)
//
//    @Delete
//    suspend fun deleteDraftModel(draftModel: DraftModel)
//
//    @Query("SELECT * FROM notes_table")
//    suspend fun getAllNotes(): List<Note>
//
//    @Transaction
//    @Query("SELECT * FROM notes_table WHERE ID = :id")
//    suspend fun getNoteWithDraftModelUsingId(id: Int): List<NoteWithDraftModel>
}