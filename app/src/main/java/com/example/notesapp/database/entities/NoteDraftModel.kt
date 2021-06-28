package com.example.notesapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteDraftModel(
    @PrimaryKey(autoGenerate = true)
    val draftID: Long,
    val ID: Int
//    val draftTitle: String
)