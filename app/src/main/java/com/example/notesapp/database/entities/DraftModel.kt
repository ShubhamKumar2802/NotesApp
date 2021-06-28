package com.example.notesapp.database.entities

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class DraftModel(
    @PrimaryKey(autoGenerate = false)
    val draftID: Long,
    val ID: Int,    // Used for NoteWithDraftModel relation
    val itemType: Int,
    val mode: Int,
    val style: Int,
    val content: String,
    @Nullable
    val imageDownloadURL: String?,
    @Nullable
    val imageCaption: String?
) : Parcelable