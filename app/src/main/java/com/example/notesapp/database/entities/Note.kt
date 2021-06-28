package com.example.notesapp.database.entities

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var ID: Int,
    var noteText: String,
    @Nullable
    var noteImageBannerURL: String?,
//    var draftID: Long
) : Parcelable