package com.example.notesapp.database.entities.entityRelations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.example.notesapp.database.entities.Note
import com.example.notesapp.database.entities.NoteDraftModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class NoteAndNoteDraftModel(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "ID",
        entityColumn = "ID"         // should not be a primary key in NoteDraftModel
    )
    val noteDraftModel: @RawValue NoteDraftModel?
) : Parcelable