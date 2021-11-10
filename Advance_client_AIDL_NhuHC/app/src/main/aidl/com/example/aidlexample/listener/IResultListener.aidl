// IResultListener.aidl
package com.example.aidlexample.listener;

// Declare any non-default types here with import statements
import com.example.aidlexample.model.Student;
interface IResultListener {
    void onResult(in String msgResult,inout Student student );
}