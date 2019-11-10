package com.example.notesapp.ui.viewmodels

import com.example.notesapp.data.NotesRepository
import com.example.notesapp.data.errors.NoAuthException
import com.example.notesapp.ui.viewstates.SplashViewState

class SplashViewModel() : BaseViewModel<Boolean?, SplashViewState>() {
    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever{
            viewStateLiveData.value = if(it != null) {
                SplashViewState(authenticated = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}