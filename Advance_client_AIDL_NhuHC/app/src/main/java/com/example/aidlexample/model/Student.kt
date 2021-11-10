package com.example.aidlexample.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator


class Student : Parcelable {
    var idStudent: Long = -1L
    var nameStudent: String? = null
    var gradeStudent: String? = null
    var math: Float = -1f
    var physic: Float = -1f
    var chemistry: Float = -1f
    var english: Float = -1f
    var literature: Float = -1f

    constructor() {}
    protected constructor(`in`: Parcel) {
        idStudent = `in`.readLong()
        nameStudent = `in`.readString()
        gradeStudent = `in`.readString()
        math = `in`.readFloat()
        physic = `in`.readFloat()
        chemistry = `in`.readFloat()
        english = `in`.readFloat()
        literature = `in`.readFloat()
    }
    fun readFromParcel(reply: Parcel?) {}
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(idStudent)
        dest.writeString(nameStudent)
        dest.writeString(gradeStudent)
        dest.writeFloat(math)
        dest.writeFloat(physic)
        dest.writeFloat(chemistry)
        dest.writeFloat(literature)
        dest.writeFloat(english)
    }

    companion object CREATOR : Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }


}