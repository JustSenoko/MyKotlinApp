package com.example.notesapp.data

import com.example.notesapp.data.provider.RemoteDataProvider

class NotesRepository(private val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    suspend fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    suspend fun saveNote(note: Note) = remoteProvider.saveNote(note)
    suspend fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)
    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()
}