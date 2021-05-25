package com.example.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM table_notes ORDER BY _id")
    fun getAllNotes(): LiveData<List<Note>>
}