package com.example.mykotlinapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mykotlinapp.data.Note
import com.example.mykotlinapp.data.NotesRepository

class NoteViewModel: ViewModel() {
    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }
}