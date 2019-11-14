package com.example.notesapp.data

import com.example.notesapp.data.provider.RemoteDataProvider

class NotesRepository(private val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
}