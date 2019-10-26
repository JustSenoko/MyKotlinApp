package com.example.mykotlinapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val viewStateLiveData : MutableLiveData<String> = MutableLiveData()

    init {
    }

    fun setValue(newValue : String) {
        viewStateLiveData.value = newValue
    }

    fun viewState() : LiveData<String> = viewStateLiveData
}