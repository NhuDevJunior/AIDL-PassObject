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
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.aidlexample.IMyAidlInterface
import com.example.aidlexample.R
import com.example.aidlexample.databinding.FragmentHomeBinding
import com.example.aidlexample.listener.IResultListener
import com.example.aidlexample.model.Student
import com.example.aidlexample.utils.Constant.ACTION_ADD
import com.example.aidlexample.utils.Constant.ACTION_NORMAL
import com.example.aidlexample.utils.Constant.ACTION_SEARCH
import com.example.aidlexample.utils.Constant.ACTION_UPDATE
import com.example.aidlexample.utils.Constant.ERROR_SEARCH
import com.example.aidlexample.utils.Constant.SUCCESS_ADD
import com.example.aidlexample.utils.Constant.SUCCESS_SEARCH
import com.example.aidlexample.utils.Constant.SUCCESS_UPDATE
import com.example.aidlexample.viewmodel.MyViewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>(),View.OnClickListener {
    private val ACTION_BIND_SERVICE = "aidlexample"
    private var iMyAidlInterface: IMyAidlInterface? = null
    private val TAG = "NhuHC"
    private var connected = false
    private val myViewModel: MyViewModel by activityViewModels()
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
        binding.tvResult.setOnClickListener(this)
    }
    private val listener: IResultListener = object : IResultListener.Stub() {
        @Throws(RemoteException::class)
        override fun onResult(msgResult:String,responseStudent: Student) {
            when (msgResult) {
                SUCCESS_ADD -> {
//                    myViewModel.setDisplayStudent(responseStudent)
                    Log.i("NhuHC", "msgResult ${responseStudent.nameStudent}")
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                }
                SUCCESS_SEARCH -> {
                    Log.i("NhuHC", "msgResult search is ${responseStudent.nameStudent}")
                    myViewModel.setDisplayStudent(responseStudent)
                }
                ERROR_SEARCH -> {
                    binding.tvResult.text = "Not found"
                }
                SUCCESS_UPDATE ->{
                    Log.i("NhuHC", "msgResult update ${responseStudent.nameStudent}")
                    Toast.makeText(requireContext(), "Update Success", Toast.LENGTH_LONG).show()
                    myViewModel.setDisplayStudent(responseStudent)
                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    fun setTextVResult(responseStudent: Student){
        binding.tvResult.text = " name:" + responseStudent.nameStudent+
                "\n grade: ${responseStudent.gradeStudent}"+"\n math: ${responseStudent.math}"+
                "\n physic: ${responseStudent.physic}"+"\n chemistry: ${responseStudent.chemistry}"+
                "\n english: ${responseStudent.english}"+"\n literature: ${responseStudent.literature}" +
                "\n click to update"
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
                Log.i("NhuHC","search")
                val entity = Student()
                entity.idStudent = 1006
                entity.nameStudent = binding.yourName.text.toString()
                entity.gradeStudent = "8A4"
                entity.math = 4f
                entity.physic = 3f
                entity.chemistry = 2f
                entity.literature = 1f
                entity.english = 0f
                val pair = Pair(ACTION_SEARCH,entity)
                myViewModel.setStudentMeta(pair)
            }
            R.id.fabAdd ->{
                AddStudentDialog().show(requireFragmentManager(),"Nhu")
            }
            R.id.tv_result ->{
                UpdateStudentDialog().show(requireFragmentManager(),"Nhu")
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        myViewModel.observerStudent.observe(viewLifecycleOwner,{
            when(it.first){
                ACTION_SEARCH ->{
                    // send data
                    try {
                        iMyAidlInterface!!.objectTypes(ACTION_SEARCH,it.second)
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
                ACTION_ADD ->{
                    // send data
                    try {
                        iMyAidlInterface!!.objectTypes(ACTION_ADD,it.second)
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
                ACTION_UPDATE ->{
                    // send data
                    try {
                        iMyAidlInterface!!.objectTypes(ACTION_UPDATE,it.second)
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
                ACTION_NORMAL ->{

                }
            }
        })
        myViewModel.observerDisplayStudent.observe(viewLifecycleOwner,{ displayStudent ->
                setTextVResult(displayStudent)
        })
    }
}