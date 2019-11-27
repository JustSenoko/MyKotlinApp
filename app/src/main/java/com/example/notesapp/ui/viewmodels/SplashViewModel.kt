package com.example.notesapp.ui.viewmodels

import com.example.notesapp.data.NotesRepository
import com.example.notesapp.data.errors.NoAuthException
import kotlinx.coroutines.launch

class SplashViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Boolean?>() {
    fun requestUser() {
        launch {
            notesRepository.getCurrentUser()?.let { setData(true) }
                    ?: setError(NoAuthException())
        }
    }
}