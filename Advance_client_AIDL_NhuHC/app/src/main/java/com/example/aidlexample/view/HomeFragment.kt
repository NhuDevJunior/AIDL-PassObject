package com.example.aidlexample.view

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aidlexample.IMyAidlInterface
import com.example.aidlexample.R
import com.example.aidlexample.databinding.FragmentHomeBinding
import com.example.aidlexample.listener.IResultListener
import com.example.aidlexample.model.Student


class HomeFragment : BaseFragment<FragmentHomeBinding>(),View.OnClickListener {
    private val ACTION_BIND_SERVICE = "aidlexample"
    private var iMyAidlInterface: IMyAidlInterface? = null
    private val TAG = "NhuHC"
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }

    override fun initAction() {
        super.initAction()
        binding.btnBasic.setOnClickListener(this)
        binding.btnBind.setOnClickListener(this)
        binding.btnEntity.setOnClickListener(this)
        binding.btnListener.setOnClickListener(this)
        binding.btnUnbind.setOnClickListener(this)
        binding.btnModel.setOnClickListener(this)
    }
    private val listener: IResultListener = object : IResultListener.Stub() {
        @Throws(RemoteException::class)
        override fun onResult(msgResult:String,responseStudent: Student) {
            binding.tvResult.setText("result msg==" + responseStudent.nameStudent)
            Log.i("NhuHC","msgResult ${responseStudent.nameStudent}")
        }
    }

    private fun bindRemoteService() {
//        explicitBind()
        implicitBind();
    }

    //显示绑定
    private fun explicitBind() {
        Log.i("NhuHC","explicitBind")
        // do something about connected remote service
        val intent = Intent(ACTION_BIND_SERVICE)
        val pack = IMyAidlInterface::class.java.`package`
        pack?.let {
            intent.setPackage(pack.name)
            requireContext().bindService(
                intent, mConnection, Context.BIND_AUTO_CREATE
            )
        }
    }


    //隐式绑定(隐式转显示)
    private fun implicitBind() {
        val intent = Intent()
        //利用intent中的Action区分查找要绑定服务
        intent.action = ACTION_BIND_SERVICE
        val explicitIntent = Intent(createExplicitFromImplicitIntent(requireContext(), intent))
        requireContext().bindService(explicitIntent, mConnection, BIND_AUTO_CREATE)
    }


    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            //必须使用IService中的静态方法转换，不能强转
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service)
            binding.tvResult.setText("onServiceConnected:$name")
            Log.i("NhuHC","success")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i("NhuHC","fail")
        }
    }

    /**
     * createExplicitFromImplicitIntent
     * 根据acton找到对应包名
     *
     * @param context
     * @param implicitIntent
     * @return
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun createExplicitFromImplicitIntent(context: Context, implicitIntent: Intent?): Intent? {
        // Retrieve all services that can match the given intent
        val pm: PackageManager = context.packageManager
        val resolveInfo = pm.queryIntentServices(implicitIntent!!, 0)
        Log.i("TTT", """resolveInfo=$resolveInfo resolveInfo.size()=${resolveInfo.size}""")
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size != 1) {
            return Intent()
        }

        // Get component info and create ComponentName
        val serviceInfo = resolveInfo[0]
        val packageName = serviceInfo.serviceInfo.packageName
        val className = serviceInfo.serviceInfo.name
        val component = ComponentName(packageName, className)

        // Create a new intent. Use the old one for extras and such reuse
        val explicitIntent = Intent(implicitIntent)
        // Set the component to be explicit
        explicitIntent.component = component
        return explicitIntent
    }
    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btn_bind ->  bindRemoteService()
            R.id.btn_unbind -> requireContext().unbindService(mConnection)
            R.id.btn_basic -> {
                try {
                    iMyAidlInterface!!.basicTypes(777, 22L, true, 2f, 2.1, "hi,this is client")
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
            R.id.btn_entity -> {
                val entity = Student()
                entity.idStudent = 1006
                entity.nameStudent = "SangHN"
                entity.gradeStudent = "8A4"
                entity.math = 4f
                entity.physic = 3f
                entity.chemistry = 2f
                entity.literature = 1f
                entity.english = 0f
                val requestCode = 0
                try {
                    iMyAidlInterface!!.objectTypes(requestCode,entity)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
            R.id.btn_listener -> {
                try {
                    iMyAidlInterface!!.callbackTypes(listener)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
            R.id.btn_model -> {
                try {
                    Log.d(TAG, "onViewClicked:model= " + iMyAidlInterface!!.model)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
    }
}