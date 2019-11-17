package com.example.notesapp.ui.activities

import com.example.notesapp.ui.viewmodels.SplashViewModel
import com.example.notesapp.ui.viewstates.SplashViewState
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {
    override val model: SplashViewModel by viewModel()
    override val layoutRes: Int? = null

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            MainActivity.start(this)
        }
    }

    override fun onResume() {
        super.onResume()
        model.requestUser()
    }

}