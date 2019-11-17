package com.example.notesapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteResult
import com.example.notesapp.data.NotesRepository
import com.example.notesapp.ui.viewstates.MainViewState

class MainViewModel(notesRepository: NotesRepository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val noteObserver = Observer<NoteResult> {
        it?: return@Observer
        when (it) {
            is NoteResult.Success<*> ->
                viewStateLiveData.value = MainViewState(notes = it.data as? List<Note>)
            is NoteResult.Error ->
                viewStateLiveData.value = MainViewState(error = it.error)
        }
    }

    private val repositoryNotes = notesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()

        repositoryNotes.observeForever(noteObserver)
    }

    fun setValue(newValue: MainViewState) {
        viewStateLiveData.value = newValue
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData

    override fun onCleared() {
        repositoryNotes.removeObserver(noteObserver)
        super.onCleared()
    }
}