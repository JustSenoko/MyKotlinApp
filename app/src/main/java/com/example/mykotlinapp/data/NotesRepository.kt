package com.example.mykotlinapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

object NotesRepository {

    private val notesLiveData = MutableLiveData<List<Note>>()

    private val notes = mutableListOf(
            Note(
                    UUID.randomUUID().toString(),
                    "Первая заметка",
                    "Текст первой заметки. Не очень длинный, но очень интересный",
                    Note.Colors.WHITE
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Вторая заметка",
                    "Текст второй заметки. Не очень длинный, но очень интересный",
                    Note.Colors.YELLOW
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Третья заметка",
                    "Текст третьей заметки. Не очень длинный, но очень интересный",
                    Note.Colors.GREEN
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Четвертая заметка",
                    "Текст четвертой заметки. Не очень длинный, но очень интересный",
                    Note.Colors.BLUE
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Пятая заметка",
                    "Текст пятой заметки. Не очень длинный, но очень интересный",
                    Note.Colors.RED
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Шестая заметка",
                    "Текст шестой заметки. Не очень длинный, но очень интересный",
                    Note.Colors.VIOLET
            )
    )

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    fun saveNote(note: Note) {
        addReplaceNote(note)
        notesLiveData.value = notes
    }

    private fun addReplaceNote(note: Note) {
        for (i in notes.indices) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }
}