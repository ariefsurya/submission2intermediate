package com.example.submissionintermediate2.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Entity(tableName = "StoryModel")
data class StoryModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @field:SerializedName("id")
    var id: String = "",

    @ColumnInfo(name = "name")
    @field:SerializedName("name")
    var name: String? = "",

    @ColumnInfo(name = "description")
    @field:SerializedName("description")
    var description: String? = "",

    @ColumnInfo(name = "photoUrl")
    @field:SerializedName("photoUrl")
    var photoUrl: String? = "",

    @ColumnInfo(name = "createdAt")
    @field:SerializedName("createdAt")
    var createdAt: String? = "",

    @ColumnInfo(name = "lat")
    @field:SerializedName("lat")
    var lat: Double? = 0.0,

    @ColumnInfo(name = "lon")
    @field:SerializedName("lon")
    var lon: Double? = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?.toString() ?: "",
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(photoUrl)
        parcel.writeString(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StoryModel> {
        override fun createFromParcel(parcel: Parcel): StoryModel {
            return StoryModel(parcel)
        }

        override fun newArray(size: Int): Array<StoryModel?> {
            return arrayOfNulls(size)
        }
    }
}