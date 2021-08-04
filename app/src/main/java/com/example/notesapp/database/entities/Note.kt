package com.example.notesapp.database.entities

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesapp.database.BannerImage
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import xute.markdeditor.models.DraftModel

@Parcelize
@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var ID: Long,
    var noteContents: @RawValue DraftModel,
    @Nullable
    var bannerImage: @RawValue BannerImage?
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }
        other as Note
        if (ID != other.ID) {
            return false
        }
        if (noteContents != other.noteContents) {
            return false
        }
        if (bannerImage != other.bannerImage) {
            return false
        }
        return true
    }
}