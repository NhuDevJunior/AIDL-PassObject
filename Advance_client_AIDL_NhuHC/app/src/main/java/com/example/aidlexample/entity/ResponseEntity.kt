package com.example.aidlexample.entity

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator


class ResponseEntity : Parcelable {
    var resultCode = 0
    var resultMsg: String? = null

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(resultCode)
        dest.writeString(resultMsg)
    }

    protected constructor(`in`: Parcel) {
        resultCode = `in`.readInt()
        resultMsg = `in`.readString()
    }

    fun readFromParcel(reply: Parcel?) {}

    companion object CREATOR : Creator<ResponseEntity> {
        override fun createFromParcel(parcel: Parcel): ResponseEntity {
            return ResponseEntity(parcel)
        }

        override fun newArray(size: Int): Array<ResponseEntity?> {
            return arrayOfNulls(size)
        }
    }


}