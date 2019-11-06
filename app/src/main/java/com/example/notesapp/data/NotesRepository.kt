package com.example.notesapp.data

import com.example.notesapp.data.provider.FireStoreProvider
import com.example.notesapp.data.provider.RemoteDataProvider

object NotesRepository {
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
}