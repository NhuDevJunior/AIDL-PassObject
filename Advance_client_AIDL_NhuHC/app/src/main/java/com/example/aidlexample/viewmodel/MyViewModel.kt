package com.example.aidlexample.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aidlexample.model.Student

class MyViewModel():ViewModel() {
    private var _studentMeta = MutableLiveData<Pair<Int,Student>>()
    val observerStudent: LiveData<Pair<Int,Student>>
        get() = _studentMeta
    fun setStudentMeta(newData:Pair<Int,Student>){
        _studentMeta.postValue(newData)
    }
    private val _displayStudent = MutableLiveData<Student>()
    val observerDisplayStudent:LiveData<Student>get() = _displayStudent
    fun setDisplayStudent(newData:Student){
        _displayStudent.postValue(newData)
    }
}