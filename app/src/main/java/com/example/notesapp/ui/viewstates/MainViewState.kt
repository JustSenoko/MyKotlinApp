package com.example.notesapp.ui.viewstates

import com.example.notesapp.data.Note

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) : BaseViewState<List<Note>?>(notes, error)