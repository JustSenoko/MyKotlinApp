package com.example.notesapp.ui.activities

import androidx.lifecycle.ViewModelProviders
import com.example.notesapp.ui.viewmodels.SplashViewModel
import com.example.notesapp.ui.viewstates.SplashViewState

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {
    override val viewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }
    override val layoutRes: Int? = null

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            MainActivity.start(this)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

}