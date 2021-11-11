package com.example.aidlexample.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "pupil_table")
data class Pupil(    @PrimaryKey(autoGenerate = true)
                     @ColumnInfo(name = "idPupil")
                     var idPupil: Long?=0,
                     @ColumnInfo(name = "namePupil")
                     val namePupil:String,
                     @ColumnInfo(name = "gradePupil")
                     val gradePupil:String,
                     @ColumnInfo(name = "math")
                     val math:Float,
                     @ColumnInfo(name = "physic")
                     val physic:Float,
                     @ColumnInfo(name = "chemistry")
                     val chemistry:Float,
                     @ColumnInfo(name = "english")
                     val english:Float,
                     @ColumnInfo(name = "literature")
                     val literature:Float
): Parcelable