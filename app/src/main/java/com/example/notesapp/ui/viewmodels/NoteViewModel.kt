package com.example.notesapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteResult
import com.example.notesapp.data.NotesRepository
import com.example.notesapp.ui.viewstates.NoteViewState

class NoteViewModel: BaseViewModel<Note?, NoteViewState>() {
    private var pendingNote: Note? = null

    init {
        viewStateLiveData.value = NoteViewState()
    }

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }

    fun loadNote(id: String) {
        NotesRepository.getNoteById(id).observeForever{
            it ?: return@observeForever
            when (it) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(note = it.data as? Note)
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = it.error)
            }
        }
    }
}