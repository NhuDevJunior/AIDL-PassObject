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
import androidx.core.view.isVisible
import com.example.aidlexample.IMyAidlInterface
import com.example.aidlexample.R
import com.example.aidlexample.databinding.FragmentHomeBinding
import com.example.aidlexample.listener.IResultListener
import com.example.aidlexample.model.Student


class HomeFragment : BaseFragment<FragmentHomeBinding>(),View.OnClickListener {
    private val ACTION_BIND_SERVICE = "aidlexample"
    private var iMyAidlInterface: IMyAidlInterface? = null
    private val TAG = "NhuHC"
    private var connected = false
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }

    override fun initAction() {
        super.initAction()
        binding.btnBind.setOnClickListener(this)
        binding.search.setOnClickListener(this)
        binding.fabAdd.setOnClickListener(this)
    }
    private val listener: IResultListener = object : IResultListener.Stub() {
        @SuppressLint("SetTextI18n")
        @Throws(RemoteException::class)
        override fun onResult(msgResult:String,responseStudent: Student) {
            binding.tvResult.text = " name:" + responseStudent.nameStudent+
                    "\n grade: ${responseStudent.gradeStudent}"+"\n math: ${responseStudent.math}"+
                    "\n physic: ${responseStudent.physic}"+"\n chemistry: ${responseStudent.chemistry}"+
                    "\n english: ${responseStudent.english}"+"\n literature: ${responseStudent.literature}"
            Log.i("NhuHC","msgResult ${responseStudent.nameStudent}")
        }
    }

    private fun bindRemoteService() {
//        explicitBind()
        implicitBind();
    }

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


    private fun implicitBind() {
        val intent = Intent()
        intent.action = ACTION_BIND_SERVICE
        val explicitIntent = Intent(createExplicitFromImplicitIntent(requireContext(), intent))
        requireContext().bindService(explicitIntent, mConnection, BIND_AUTO_CREATE)
    }


    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service)
            Log.i("NhuHC","success")
            connected = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i("NhuHC","fail")
            connected = false
        }
    }

    /**
     * createExplicitFromImplicitIntent
     *
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
            R.id.btn_bind ->  {
                if(connected){
                    requireContext().unbindService(mConnection)
                    binding.btnBind.text = "connect"
                    connected = false
                    binding.containerManagement.isVisible = false
                }else{
                    bindRemoteService()
                    binding.btnBind.text = "disconnect"
                    binding.containerManagement.isVisible = true
                }

            }
            R.id.search -> {
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
                // send data
                try {
                    iMyAidlInterface!!.objectTypes(requestCode,entity)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
                // call back data
                try {
                    iMyAidlInterface!!.callbackTypes(listener)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
            R.id.fabAdd ->{
                AddStudentDialog().show(requireFragmentManager(),"Nhu")
            }
        }
    }
}