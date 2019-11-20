package com.example.notesapp.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteResult
import com.example.notesapp.data.NotesRepository
import io.mockk.*
import org.junit.Assert.*
import org.junit.*

class MainViewModelTest {

    @get: Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val notesLiveData = MutableLiveData<NoteResult>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        clearAllMocks()
        every { mockRepository.getNotes() } returns notesLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1){mockRepository.getNotes()}
    }

    @Test
    fun `should return notes`() {
        var result: List<Note>? = null
        val testNotes = listOf(Note("1"), Note("2"))
        viewModel.getViewState().observeForever {
            result = it?.data
        }
        notesLiveData.value = NoteResult.Success(testNotes)
        assertEquals(testNotes, result)
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        notesLiveData.value = NoteResult.Error(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assertFalse(notesLiveData.hasObservers())
    }
}