package com.example.notesapp.database.entities

import androidx.room.TypeConverter
import com.example.notesapp.database.BannerImage
import com.google.gson.Gson
import xute.markdeditor.models.DraftModel

class Converters {

    @TypeConverter
    fun draftModelToString(draftModel: DraftModel): String {
        return Gson().toJson(draftModel)
    }

    @TypeConverter
    fun stringToDraftModel(noteContent: String): DraftModel {
        return Gson().fromJson(noteContent, DraftModel::class.java)
    }

    @TypeConverter
    fun bannerImageToString(bannerImage: BannerImage?): String? {
        bannerImage?.apply {
            return Gson().toJson(bannerImage)
        }
        return null
    }

    @TypeConverter
    fun stringToBannerImage(imageContents: String?): BannerImage? {
        imageContents?.apply {
            return Gson().fromJson(imageContents, BannerImage::class.java)
        }
        return null
    }
}