// IResultListener.aidl
package com.example.aidlexample.listener;

// Declare any non-default types here with import statements
import com.example.aidlexample.entity.ResponseEntity;
interface IResultListener {
    void onResult(inout ResponseEntity responseEntity );
}