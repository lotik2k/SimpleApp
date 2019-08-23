package com.movista.app.presentation.base

import android.os.Bundle

import com.arellomobile.mvp.MvpAppCompatActivity

abstract class BaseActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        onActivityInject()

        super.onCreate(savedInstanceState)
        setupContentView()

        initUI()
        afterCreate()
    }

    abstract fun getLayoutRes(): Int

    protected abstract fun onActivityInject()

    protected open fun initUI() {}

    protected open fun afterCreate() {}

    private fun setupContentView() {
        setContentView(getLayoutRes())
    }
}