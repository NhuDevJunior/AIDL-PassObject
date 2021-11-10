package com.example.aidlexample.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator


class Student : Parcelable {
    var idStudent = 0L
    var nameStudent: String? = null
    var gradeStudent: String? = null
    var math: Float = 0f
    var physic: Float = 0f
    var chemistry: Float = 0f
    var english: Float = 0f
    var literature: Float = 0f
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

    override fun describeContents(): Int {
        return 0
    }
    fun readFromParcel(reply: Parcel?) {}
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(idStudent)
        dest.writeString(nameStudent)
        dest.writeString(gradeStudent)
        dest.writeFloat(math)
        dest.writeFloat(physic)
        dest.writeFloat(chemistry)
        dest.writeFloat(english)
        dest.writeFloat(literature)
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