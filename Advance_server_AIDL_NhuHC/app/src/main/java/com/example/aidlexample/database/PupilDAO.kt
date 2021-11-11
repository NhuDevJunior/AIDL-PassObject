package com.example.aidlexample.database

import androidx.room.*
import com.example.aidlexample.model.Pupil
import kotlinx.coroutines.flow.Flow


@Dao
interface PupilDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPupil(pupil: Pupil):Long
    @Query("SELECT * FROM pupil_table WHERE namePupil = :name")
    fun findPupil(name:String):List<Pupil>
    @Update
    fun updatePupil(pupil: Pupil):Int
}