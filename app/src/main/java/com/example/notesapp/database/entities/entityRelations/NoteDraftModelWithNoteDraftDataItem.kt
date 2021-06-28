package com.example.notesapp.database.entities.entityRelations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.notesapp.database.entities.NoteDraftDataItem
import com.example.notesapp.database.entities.NoteDraftModel

data class NoteDraftModelWithNoteDraftDataItem(
    @Embedded val noteDraftModel: NoteDraftModel,
    @Relation(
        parentColumn = "draftID",       // Primary key in NoteDraftModel
        entityColumn = "draftID"        // Not a Primary key in NoteDraftDataItem
    )
    val draftDataItem: List<NoteDraftDataItem>
)