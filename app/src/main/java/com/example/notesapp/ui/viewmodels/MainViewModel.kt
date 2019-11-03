package com.example.notesapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp.data.NotesRepository
import com.example.notesapp.ui.viewstates.MainViewState

class MainViewModel : ViewModel() {
    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        NotesRepository.getNotes().observeForever { notes ->
            notes?.let {
                viewStateLiveData.value = viewStateLiveData.value?.copy(notes = it)
                        ?: MainViewState(it)
            }
        }
    }

    fun setValue(newValue: MainViewState) {
        viewStateLiveData.value = newValue
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}