package com.example.notesapp.data.provider

import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteResult
import com.example.notesapp.data.User
import kotlinx.coroutines.channels.ReceiveChannel

interface RemoteDataProvider {
    fun subscribeToAllNotes() : ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String) : Note
    suspend fun saveNote(note: Note) : Note
    suspend fun deleteNote(noteId: String)
    suspend fun getCurrentUser(): User?
}