package com.example.notesapp.ui.viewmodels

import androidx.annotation.VisibleForTesting
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteResult
import com.example.notesapp.data.NotesRepository
import com.example.notesapp.ui.viewstates.NoteData
import kotlinx.coroutines.launch

class NoteViewModel(private val notesRepository: NotesRepository) : BaseViewModel<NoteData>() {
    private val currentNote: Note?
        get() = getViewStateChannel().poll()?.note

    fun save(note: Note) {
        setData(NoteData(note = note))
    }

    @VisibleForTesting
    public override fun onCleared() {
        launch {
            currentNote?.let { notesRepository.saveNote(it) }
            super.onCleared()
        }
    }

    fun loadNote(id: String) {
        launch {
            try {
                setData(NoteData(note = notesRepository.getNoteById(id)))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun deleteNote() {
        launch {
            try {
                currentNote?.let { notesRepository.deleteNote(it.id) }
                setData(NoteData(isDeleted = true))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }
}