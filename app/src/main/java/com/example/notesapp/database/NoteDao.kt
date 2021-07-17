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

    // UPDATE
    @Query("UPDATE notes_table SET noteText = :noteText, noteImageBannerURL = :bannerImageUrl WHERE ID = :id")
    suspend fun updateNote(id: Long, noteText: String, bannerImageUrl: String?)

    @Query(
        """
           UPDATE draftmodel
           SET itemType = :itemType, 
           mode = :mode, 
           style = :style, 
           content = :content, 
           imageDownloadURL = :imageUrl, 
           imageCaption = :caption
           WHERE draftID = :id
        """
    )
    suspend fun updateDraftModel(
        id: Long,
        itemType: Int,
        mode: Int,
        style: Int,
        content: String,
        imageUrl: String?,
        caption: String?
    )

    @Transaction
    @Query("SELECT * FROM notes_table ORDER BY ID DESC")
    fun getAllNotes(): LiveData<List<NoteWithDraftModel>>

    @Transaction
    @Query("SELECT ID FROM notes_table WHERE noteText = :noteText")
    suspend fun getNoteIdUsingNoteText(noteText: String): Long

    @Transaction
    @Query("SELECT * FROM notes_table WHERE ID = :id")
    suspend fun getNoteWithDraftModelUsingId(id: Long): List<NoteWithDraftModel>

    @Query("SELECT * FROM notes_table WHERE ID = :id")
    suspend fun getNoteUsingId(id: Long): Note
}