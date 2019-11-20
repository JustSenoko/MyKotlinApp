package com.example.notesapp.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteResult
import com.example.notesapp.data.NotesRepository
import com.example.notesapp.ui.viewstates.NoteViewState
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Rule

class NoteViewModelTest {

    @get: Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>(relaxed = true)
    private val noteLiveData = MutableLiveData<NoteResult>()

    private val testNote = Note("1", "title", "text", Note.Colors.YELLOW)

    private lateinit var viewModel: NoteViewModel

    @Before
    fun setUp() {
        clearAllMocks()
        every { mockRepository.getNoteById(testNote.id) } returns noteLiveData
        every { mockRepository.deleteNote(testNote.id) } returns noteLiveData
        viewModel = NoteViewModel(mockRepository)
    }

    @Test
    fun `should save note`() {
        viewModel.save(testNote)
        viewModel.onCleared()
        verify(exactly = 1) { mockRepository.saveNote(testNote) }
    }

    @Test
    fun `loadNote should return note data`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(false, testNote)
        viewModel.getViewState().observeForever {
            result = it?.data
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)
        assertEquals(testData, result)
    }

    @Test
    fun `loadNote should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Error(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `deleteNote should return note data is Deleted`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(true, null)
        viewModel.getViewState().observeForever {
            result = it?.data
        }
        viewModel.save(testNote)
        viewModel.deleteNote()
        noteLiveData.value = NoteResult.Success(null)
        assertEquals(testData, result)
    }

    @Test
    fun `deleteNote should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        viewModel.save(testNote)
        viewModel.deleteNote()
        noteLiveData.value = NoteResult.Error(testData)
        assertEquals(testData, result)
    }
}