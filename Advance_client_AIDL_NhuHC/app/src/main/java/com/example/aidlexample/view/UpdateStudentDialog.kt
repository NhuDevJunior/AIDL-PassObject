package com.example.aidlexample.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.aidlexample.R
import com.example.aidlexample.databinding.DialogUpdateStudentBinding
import com.example.aidlexample.model.Student
import com.example.aidlexample.utils.Constant
import com.example.aidlexample.viewmodel.MyViewModel
import java.lang.NumberFormatException

class UpdateStudentDialog: DialogFragment() {
    private val myViewModel: MyViewModel by activityViewModels()
    private lateinit var binding: DialogUpdateStudentBinding
    private var idStudent = -1L
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        binding = DialogUpdateStudentBinding.inflate(inflater,container,false)
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
        observerData()
        initAction()
    }

    private fun observerData() {
        myViewModel.observerDisplayStudent.observe(viewLifecycleOwner,{ displayStudent ->
            setTextVResult(displayStudent)
            idStudent = displayStudent.idStudent
        })
    }

    private fun setTextVResult(displayStudent: Student) {
            binding.apply {
                etNameStudent.setText(displayStudent.nameStudent)
                etGradeStudent.setText(displayStudent.gradeStudent)
                etScoreMath.setText(displayStudent.math.toString())
                etScorePhysic.setText(displayStudent.physic.toString())
                etScoreChemistry.setText(displayStudent.chemistry.toString())
                etScoreEnglish.setText(displayStudent.english.toString())
                etScoreLiterature.setText(displayStudent.literature.toString())

            }
    }

    private fun initAction() {
        binding.btnUpdate.setOnClickListener{
            binding.apply {
                try{
                    val student = Student()
                    student.idStudent = idStudent
                    student.nameStudent = etNameStudent.text.toString()
                    student.gradeStudent = etGradeStudent.text.toString()
                    student.math = etScoreMath.text.toString().toFloat()
                    student.physic = etScorePhysic.text.toString().toFloat()
                    student.chemistry = etScoreChemistry.text.toString().toFloat()
                    student.english = etScoreEnglish.text.toString().toFloat()
                    student.literature = etScoreLiterature.text.toString().toFloat()
                    val pair = Pair(Constant.ACTION_UPDATE,student)
                    myViewModel.setStudentMeta(pair)
                    Log.i("NhuHC","update id student $idStudent")
                }catch (e: NumberFormatException){
                    Toast.makeText(requireContext(), "Input Invalid", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}