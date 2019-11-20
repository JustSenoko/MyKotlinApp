package com.example.notesapp.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteResult
import com.example.notesapp.data.errors.NoAuthException
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class FireStoreProviderTest {

    @get: Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockResultCollection = mockk<CollectionReference>()
    private val mockUser = mockk<FirebaseUser>()

    private val provider = FireStoreProvider(mockAuth, mockDb)

    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()
    private val testNotes = listOf(
            Note("1"),
            Note("2"),
            Note("3")
    )

    @Before
    fun setUp() {
        clearMocks()
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns "1"
        every { mockDb.collection(any()).document(any()).collection(any()) } returns mockResultCollection

        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `should throw NoAuthException`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null
        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeToAllNotes returns notes`() {
        var result: List<Note>? = null
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)

        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Success<List<Note>>)?.data
        }
        slot.captured.onEvent(mockSnapshot, null)

        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeToAllNotes returns error`() {
        var result: Throwable? = null
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        slot.captured.onEvent(null, testError)

        assertEquals(testError, result)
    }

    @Test
    fun `saveNote calls set`() {
        val mockDocumentReference = mockk<DocumentReference>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        provider.saveNote(testNotes[0])
        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote returns note`() {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<in Void>>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)) } returns mockk()

        provider.saveNote(testNotes[0]).observeForever {
            result = (it as? NoteResult.Success<Note>)?.data
        }
        slot.captured.onSuccess(null)

        assertEquals(testNotes[0], result)
    }

    @Test
    fun `deleteNote calls delete` () {
        val mockDocumentReference = mockk<DocumentReference>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        provider.deleteNote(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.delete() }
    }

    @Test
    fun `deleteNote returns null`() {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<in Void>>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        every { mockDocumentReference.delete().addOnSuccessListener(capture(slot)) } returns mockk()

        provider.deleteNote(testNotes[0].id).observeForever {
            result = (it as? NoteResult.Success<Note>)?.data
        }
        slot.captured.onSuccess(null)

        assertEquals(null, result)
    }
}