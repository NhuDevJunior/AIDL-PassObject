package com.example.aidlexample

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.example.aidlexample.entity.RequestEntity
import com.example.aidlexample.entity.ResponseEntity
import com.example.aidlexample.listener.IResultListener


class AidlService : Service() {
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
        override fun objectTypes(requestEntity: RequestEntity) {
            Log.d(TAG, "objectTypes:requestEntity.getRequestMsg=== " + requestEntity.requestMsg)
        }

        @Throws(RemoteException::class)
        override fun callbackTypes(listener: IResultListener) {
            val entity = ResponseEntity()
            entity.resultCode = 0
            entity.resultMsg = "Result OK"
            listener.onResult(entity)
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

