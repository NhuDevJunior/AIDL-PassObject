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
import com.example.aidlexample.utils.Constant.ERROR_ADD
import com.example.aidlexample.utils.Constant.SUCCESS_ADD
import kotlinx.coroutines.*


class AidlService : Service() {
    private var pupil:Pupil?=null
    private var coroutineScope = CoroutineScope(Dispatchers.Main+ Job())
    private var job: Job? =null
    var result:Long?=null
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
            requestEntity.apply {
            pupil = nameStudent?.let {
                gradeStudent?.let { it1 ->
                    Pupil(null,
                        it, it1,math,physic,chemistry,english,literature)
                }
            }
            }
            if(requestCode==ACTION_ADD) {
                job = coroutineScope.launch {
                    result = pupil?.let { Repository.addPupil(it) }
                }
            }
        }

        @Throws(RemoteException::class)
        override fun callbackTypes(listener: IResultListener) {
            val entity = Student()
            entity.idStudent = pupil?.idPupil?:0L
            entity.nameStudent = pupil?.namePupil
            entity.gradeStudent = pupil?.gradePupil
            entity.math = pupil?.math?:0f
            entity.physic = pupil?.physic?:0f
            entity.chemistry = pupil?.chemistry?:0f
            entity.literature = pupil?.literature?:0f
            entity.english = pupil?.english?:0f
            var msgResult = ""
            msgResult = if(result==-1L){
                ERROR_ADD
            } else {
                SUCCESS_ADD
            }
            listener.onResult(msgResult,entity)
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

