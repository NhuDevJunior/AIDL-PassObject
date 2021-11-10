package com.example.aidlexample.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aidlexample.model.Pupil
import kotlinx.coroutines.flow.Flow


@Dao
interface PupilDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPupil(pupil: Pupil):Long
}