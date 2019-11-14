package com.example.notesapp

import androidx.multidex.MultiDexApplication
import com.example.notesapp.di.appModule
import com.example.notesapp.di.mainModule
import com.example.notesapp.di.noteModule
import com.example.notesapp.di.splashModule
import com.github.ajalt.timberkt.Timber
import org.koin.android.ext.android.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(timber.log.Timber.DebugTree())
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}