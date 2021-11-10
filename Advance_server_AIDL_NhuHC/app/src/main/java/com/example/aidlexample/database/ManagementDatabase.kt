package com.example.aidlexample.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aidlexample.model.Pupil

/**
 * using version 1 because it is the first version
 */
@Database(entities = [Pupil::class], version = 1, exportSchema = false)
abstract class ManagementDatabase: RoomDatabase() {
    abstract fun pupilDao(): PupilDAO
    companion object {
        fun getInstance(context: Context): ManagementDatabase {
            return Room.databaseBuilder(context, ManagementDatabase::class.java,
                "management_database").fallbackToDestructiveMigration().build()
        }
    }
}