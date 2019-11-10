package com.example.notesapp.ui.viewstates

import com.example.notesapp.data.Note

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)