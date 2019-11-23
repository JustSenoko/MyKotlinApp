package com.example.notesapp.ui.viewstates

import com.example.notesapp.data.Note

data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)