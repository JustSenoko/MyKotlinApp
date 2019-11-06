package com.example.notesapp.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteResult
import com.github.ajalt.timberkt.Timber
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener { snapshot, exception ->
            exception?.let { result.value = NoteResult.Error(it) }
                    ?: let {
                        snapshot?.let {
                            val notes = mutableListOf<Note>()
                            for (doc: QueryDocumentSnapshot in snapshot) {
                                notes.add(doc.toObject(Note::class.java))
                            }
                            result.value = NoteResult.Success(notes)
                        }
                    }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
                .addOnSuccessListener { documentSnapshot ->
                    result.value = NoteResult.Success(documentSnapshot.toObject(Note::class.java))
                }
                .addOnFailureListener { result.value = NoteResult.Error(it) }
        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id).set(note)
                .addOnSuccessListener {
                    Timber.d { "Note $note saved" }
                    result.value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    Timber.d { "Error saving note $note, message: ${it.message}" }
                    result.value = NoteResult.Error(it)
                }
        return result
    }
}