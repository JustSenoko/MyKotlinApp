package com.example.notesapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.notesapp.data.Note
import com.example.notesapp.data.NotesRepository

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