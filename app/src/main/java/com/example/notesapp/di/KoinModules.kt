package com.example.notesapp.di

import com.example.notesapp.data.NotesRepository
import com.example.notesapp.data.provider.FireStoreProvider
import com.example.notesapp.data.provider.RemoteDataProvider
import com.example.notesapp.ui.viewmodels.MainViewModel
import com.example.notesapp.ui.viewmodels.NoteViewModel
import com.example.notesapp.ui.viewmodels.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single <RemoteDataProvider> { FireStoreProvider(get(), get()) }
    single { NotesRepository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}