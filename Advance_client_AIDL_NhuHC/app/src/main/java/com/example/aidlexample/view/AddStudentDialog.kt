package com.example.aidlexample.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.aidlexample.R
import com.example.aidlexample.databinding.DialogAddStudentBinding
import com.example.aidlexample.model.Student
import com.example.aidlexample.utils.Constant.ACTION_ADD
import com.example.aidlexample.viewmodel.MyViewModel
import java.lang.NumberFormatException

class AddStudentDialog:DialogFragment() {
    private lateinit var binding:DialogAddStudentBinding
    private val myViewModel:MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        binding = DialogAddStudentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.6).toInt()
        dialog!!.window?.setLayout(width, height)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
    }

    private fun initAction() {
        binding.btnAdd.setOnClickListener{
            binding.apply {
                try{
              val student = Student()
                student.idStudent = -1
                student.nameStudent = etNameStudent.text.toString()
                student.gradeStudent = etGradeStudent.text.toString()
                student.math = etScoreMath.text.toString().toFloat()
                student.physic = etScorePhysic.text.toString().toFloat()
                student.chemistry = etScoreChemistry.text.toString().toFloat()
                student.english = etScoreEnglish.text.toString().toFloat()
                student.literature = etScoreLiterature.text.toString().toFloat()
                val pair = Pair(ACTION_ADD,student)
                myViewModel.setStudentMeta(pair)
                }catch (e:NumberFormatException){
                    Toast.makeText(requireContext(), "Input Invalid", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}