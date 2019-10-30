package com.example.mykotlinapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinapp.data.NotesRepository
import com.example.mykotlinapp.ui.viewstates.MainViewState

class MainViewModel : ViewModel() {
    private val viewStateLiveData : MutableLiveData<MainViewState> = MutableLiveData()

    init {
        viewStateLiveData.value = MainViewState(NotesRepository.notes)
    }

    fun setValue(newValue : MainViewState) {
        viewStateLiveData.value = newValue
    }

    fun viewState() : LiveData<MainViewState> = viewStateLiveData
}