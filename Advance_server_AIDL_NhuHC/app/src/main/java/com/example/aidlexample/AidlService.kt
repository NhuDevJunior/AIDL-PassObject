package com.example.aidlexample

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.example.aidlexample.model.Student
import com.example.aidlexample.listener.IResultListener
import com.example.aidlexample.model.Pupil
import com.example.aidlexample.repo.Repository
import com.example.aidlexample.utils.Constant.ACTION_ADD
import com.example.aidlexample.utils.Constant.ACTION_SEARCH
import com.example.aidlexample.utils.Constant.ACTION_UPDATE
import com.example.aidlexample.utils.Constant.ERROR_ADD
import com.example.aidlexample.utils.Constant.ERROR_SEARCH
import com.example.aidlexample.utils.Constant.ERROR_UPDATE
import com.example.aidlexample.utils.Constant.SUCCESS_ADD
import com.example.aidlexample.utils.Constant.SUCCESS_SEARCH
import com.example.aidlexample.utils.Constant.SUCCESS_UPDATE
import kotlinx.coroutines.*


class AidlService : Service() {
    private var pupil:Pupil?=null
    private var coroutineScope = CoroutineScope(Dispatchers.Main+ Job())
    private var job: Job? =null
    var resultAdd:Long?=null
    var msgResultAdd = ""
    var resultUpdate:Int?=null
    var msgResultUpdate = ""
    var msgResultSearch = ""
    private var action:Int?=null
    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind====")
        return MyBinder()
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind====")
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate====")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy====")
        job?.cancel()
        coroutineScope.cancel()
        super.onDestroy()
    }

    private inner class MyBinder : IMyAidlInterface.Stub() {
        @Throws(RemoteException::class)
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String,
        ) {
            Log.d(TAG, "basicTypes: server received :$aString")
        }

        @Throws(RemoteException::class)
        override fun objectTypes(requestCode:Int,requestEntity: Student) {
            Log.d(TAG, "name student request is === " + requestEntity.nameStudent)
            Log.d(TAG, "request code $requestCode")
            action = requestCode
            requestEntity.apply {
            pupil = nameStudent?.let {
                gradeStudent?.let { it1 ->
                    Pupil(null,
                        it, it1,math,physic,chemistry,english,literature)
                }
            }
            }
            if(requestCode== ACTION_UPDATE){
                pupil?.idPupil = requestEntity.idStudent
            }
        }

        @Throws(RemoteException::class)
        override fun callbackTypes(listener: IResultListener) {
            val entity = Student()

            Log.i(TAG,"action is $action")
            when (action) {
                ACTION_ADD -> {
                    job = coroutineScope.launch {
                        resultAdd = pupil?.let { Repository.addPupil(it) }
                    }
                    msgResultAdd = if (resultAdd == -1L) {
                        ERROR_ADD
                    } else {
                        SUCCESS_ADD
                    }
                    pupil?.let { initValue(entity, it) }
                    listener.onResult(msgResultAdd , entity)
                }
                ACTION_SEARCH -> {
                    Log.i(TAG,"tao dang search")
                    job = coroutineScope.launch {
                        val listPupil = pupil?.namePupil?.let { Repository.searchPupil(it) }
                        if(!listPupil.isNullOrEmpty()){
                            pupil = listPupil[0]
                            msgResultSearch = SUCCESS_SEARCH
                            Log.i(TAG,"tao tim duoc roi nhe")
                            pupil?.let { initValue(entity, it) }
                        }else{
                            msgResultSearch = ERROR_SEARCH
                            Log.i(TAG,"tao eo tim duoc")
                        }
                        Log.i(TAG,"tao la message $msgResultSearch")
                        Log.i(TAG,"id pupil ${entity.idStudent}")
                        listener.onResult(msgResultSearch, entity)
                    }
                }
                ACTION_UPDATE->{
                    job = coroutineScope.launch {
                        resultUpdate = pupil?.let { Repository.updatePupil(it) }
                    }
                    msgResultUpdate = if (resultUpdate == -1) {
                        ERROR_UPDATE
                    } else {
                        SUCCESS_UPDATE
                    }
                    pupil?.let { initValue(entity, it) }
                    listener.onResult(msgResultUpdate , entity)
                }
                else -> {

                }
            }
        }
        private fun initValue(entity:Student,pupil: Pupil){
            entity.idStudent = pupil.idPupil ?:0L
            entity.nameStudent = pupil.namePupil
            entity.gradeStudent = pupil.gradePupil
            entity.math = pupil.math
            entity.physic = pupil.physic
            entity.chemistry = pupil.chemistry
            entity.literature = pupil.literature
            entity.english = pupil.english?:0f
        }
        override fun getModel(): String {
            Log.d(TAG, "basicTypes: MODEL== " + Build.MODEL)
            Log.d(TAG, "basicTypes: BRAND== " + Build.BRAND)
            return Build.MODEL
        }
    }

    companion object {
        private const val TAG = "AidlService"
    }

}

