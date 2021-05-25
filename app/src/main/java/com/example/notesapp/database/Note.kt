package com.example.notesapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_notes")
class Note(@ColumnInfo(name = "text") val text: String) {

    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0
}