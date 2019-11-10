package com.example.notesapp.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.notesapp.ui.viewmodels.BaseViewModel
import com.example.notesapp.ui.viewstates.BaseViewState


abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let {
            setContentView(it)
        }
        viewModel.getViewState().observe(this, Observer<S>{
            it ?: return@Observer
            it.error?.let {
                renderError(it)
                return@Observer
            }
            renderData(it.data)
        })
    }

    abstract fun renderData(data: T)

    protected fun renderError(error: Throwable?) = error?.let {
        it.message?.let {message ->
            showError(message)
        }
    }

    protected fun showError(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}