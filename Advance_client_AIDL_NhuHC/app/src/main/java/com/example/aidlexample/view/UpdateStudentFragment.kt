package com.example.aidlexample.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aidlexample.R
import com.example.aidlexample.databinding.FragmentUpdateStudentBinding

class UpdateStudentFragment : BaseFragment<FragmentUpdateStudentBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentUpdateStudentBinding {
        return FragmentUpdateStudentBinding.inflate(inflater,container,false)
    }

}