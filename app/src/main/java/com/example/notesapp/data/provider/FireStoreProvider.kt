package com.example.notesapp.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteResult
import com.example.notesapp.data.User
import com.example.notesapp.data.errors.NoAuthException
import com.github.ajalt.timberkt.Timber
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val store: FirebaseFirestore) : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val currentUser
        get() = firebaseAuth.currentUser

    override fun getCurrentUser() = MutableLiveData<User?>().apply {
        value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
    }

    private fun getUserNotesCollection() = currentUser?.let {
        store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun subscribeToAllNotes(): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection().addSnapshotListener { snapshot, exception ->
                exception?.let { value = NoteResult.Error(it) }
                        ?: let {
                            snapshot?.let {
                                val notes = it.documents.map { it.toObject(Note::class.java) }
                                value = NoteResult.Success(notes)
                            }
                        }
            }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun getNoteById(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection().document(id).get()
                    .addOnSuccessListener { documentSnapshot ->
                        value = NoteResult.Success(documentSnapshot.toObject(Note::class.java))
                    }
                    .addOnFailureListener { value = NoteResult.Error(it) }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun saveNote(note: Note): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection().document(note.id).set(note)
                    .addOnSuccessListener {
                        Timber.d { "Note $note saved" }
                        value = NoteResult.Success(note)
                    }
                    .addOnFailureListener {
                        Timber.d { "Error saving note $note, message: ${it.message}" }
                        value = NoteResult.Error(it)
                    }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun deleteNote(noteId: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection().document(noteId).delete()
                    .addOnSuccessListener {
                        Timber.d { "Note $noteId deleted" }
                        value = NoteResult.Success(null)
                    }
                    .addOnFailureListener {
                        Timber.d { "Error deleting note $noteId, message: ${it.message}" }
                        value = NoteResult.Error(it)
                    }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }
}