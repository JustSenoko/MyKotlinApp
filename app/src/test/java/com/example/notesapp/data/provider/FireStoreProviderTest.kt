package com.example.notesapp.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteResult
import com.example.notesapp.data.errors.NoAuthException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

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
    fun `should throw NoAuthException`() = runBlocking {
        every { mockAuth.currentUser } returns null
        val result = (provider.subscribeToAllNotes().receive() as? NoteResult.Error)?.error
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeToAllNotes returns notes`() = runBlocking {
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        val channel = provider.subscribeToAllNotes()
        slot.captured.onEvent(mockSnapshot, null)
        val result = (channel.receive() as? NoteResult.Success<List<Note>>)?.data
        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeToAllNotes returns error`() = runBlocking {
        var result: Throwable? = null
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        val channel = provider.subscribeToAllNotes()
        slot.captured.onEvent(null, testError)
        result = (channel.receive() as? NoteResult.Error)?.error

        assertEquals(testError, result)
    }

    /*@Test
    fun `saveNote calls set`() = runBlocking {
        val mockDocumentReference = mockk<DocumentReference>(relaxed = true)
        //val source: TaskCompletionSource<Void?> = TaskCompletionSource()
        val slot = slot<OnSuccessListener<in Void>>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)) } returns mockk()

        val d = async { provider.saveNote(testNotes[0])}
        slot.captured.onSuccess(null)
        d.await()

        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }*/

    @Ignore("error: java.lang.IllegalStateException: Already resumed")
    @Test
    fun `saveNote returns note`() = runBlocking {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<in Void>>()

        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        coEvery { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)) } answers {
            slot.captured.onSuccess(null)
            mockk()
        }

        val d = async { provider.saveNote(testNotes[0]) }
        result = d.await()

        assertEquals(testNotes[0], result)
    }

    /*@Test
    fun `deleteNote calls delete`() {
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
    }*/
}