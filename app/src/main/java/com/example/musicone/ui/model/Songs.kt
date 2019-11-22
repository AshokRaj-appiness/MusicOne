package com.example.musicone.ui.model

import android.os.Parcel
import android.os.Parcelable

class Songs(var songId:Long?,var songTitle:String?,var artist:String?,var songData:String?,var dateAdded:Long?):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeValue(songId)
        p0?.writeString(songTitle)
        p0?.writeString(artist)
        p0?.writeString(songData)
        p0?.writeValue(dateAdded)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Songs> {
        override fun createFromParcel(parcel: Parcel): Songs {
            return Songs(parcel)
        }

        override fun newArray(size: Int): Array<Songs?> {
            return arrayOfNulls(size)
        }
    }
}