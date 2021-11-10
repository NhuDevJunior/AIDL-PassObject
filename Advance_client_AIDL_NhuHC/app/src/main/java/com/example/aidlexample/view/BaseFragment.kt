package com.example.aidlexample.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * abstract class BaseFragment.
 */
abstract class BaseFragment <out T: ViewBinding>: Fragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    /**
     * abstract function createBinding.
     * @param inflater LayoutInflater.
     * @param container ViewGroup?.
     * @param savedInstanceState Bundle?.
     * @return ViewBinding
     */
    abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        initData()
        initView()
        initAction()
    }

    /**
     * open fun initAction. call back
     */
    open fun initData() = Unit
    /**
     * open fun initView. call back
     */
    open fun initView() = Unit
    /**
     * check Action
     */
    open fun initAction() = Unit

    /**
     * observer Live Data
     */
    open fun observeLiveData() = Unit
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}