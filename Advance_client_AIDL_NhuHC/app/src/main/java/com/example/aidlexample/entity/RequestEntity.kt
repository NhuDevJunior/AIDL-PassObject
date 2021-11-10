package com.example.aidlexample.entity

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator


class RequestEntity : Parcelable {
    var requestMsg: String? = null
    var requestCode: String? = null

    constructor() {}
    protected constructor(`in`: Parcel) {
        requestMsg = `in`.readString()
        requestCode = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(requestMsg)
        dest.writeString(requestCode)
    }

    companion object CREATOR : Creator<RequestEntity> {
        override fun createFromParcel(parcel: Parcel): RequestEntity {
            return RequestEntity(parcel)
        }

        override fun newArray(size: Int): Array<RequestEntity?> {
            return arrayOfNulls(size)
        }
    }


}