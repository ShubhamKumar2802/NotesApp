package com.example.notesapp.database.entities.entityRelations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.example.notesapp.database.entities.DraftModel
import com.example.notesapp.database.entities.Note
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteWithDraftModel(
    @Embedded
    val note: Note,
    @Relation(
        entityColumn = "ID",
        parentColumn = "ID"
    )
    val draftModel: List<DraftModel>
) : Parcelable
