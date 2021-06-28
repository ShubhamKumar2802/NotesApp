package com.example.notesapp.database.entities

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteDraftDataItem(
    @PrimaryKey(autoGenerate = true)
    val draftDataItemID: Int,
    val draftID: Long,
    val itemType: Int,
    val mode: Int,
    val style: Int,
    val content: String,
    @Nullable
    val imageDownloadURL: String?,
    @Nullable
    val imageCaption: String?
)

//(primaryKeys = ["draftDataItemID", "draftID"])