package com.example.aidlexample.repo

import com.example.aidlexample.ApplicationContext
import com.example.aidlexample.database.ManagementDatabase
import com.example.aidlexample.model.Pupil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {
    private val context = ApplicationContext.getContext()
    private val pupilDAO = ManagementDatabase.getInstance(context).pupilDao()
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    suspend fun addPupil(inputPupil:Pupil):Long{
        return withContext(ioDispatcher){
            return@withContext pupilDAO.insertPupil(inputPupil)
        }
    }
    suspend fun searchPupil(name:String):List<Pupil>{
        return withContext(ioDispatcher){
            return@withContext pupilDAO.findPupil(name)
        }
    }
    suspend fun updatePupil(inputPupil: Pupil):Int{
        return withContext(ioDispatcher){
            return@withContext pupilDAO.updatePupil(inputPupil)
        }
    }
}