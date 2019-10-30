package com.example.mykotlinapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mykotlinapp.R
import com.example.mykotlinapp.ui.adapters.NotesRVAdapter
import com.example.mykotlinapp.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRVAdapter()
        rv_notes.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.viewState().observe(this, Observer {
            it?.let { adapter.notes = it.notes }
        })

    }
}
