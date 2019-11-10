package com.example.notesapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.notesapp.R
import com.example.notesapp.data.Note
import com.example.notesapp.ui.viewmodels.NoteViewModel
import com.example.notesapp.ui.viewstates.NoteViewState
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<Note?, NoteViewState>() {
    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.note"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) =
                Intent(context, NoteActivity::class.java).run {
                    putExtra(EXTRA_NOTE, noteId)
                    context.startActivity(this)
                }
    }

    override val layoutRes: Int = R.layout.activity_note
    override val viewModel: NoteViewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }
    var note: Note? = null

    private val textChangedListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            saveNote()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let {
            viewModel.loadNote(it)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note)
        }
    }

    override fun renderData(data: Note?) {
        note = data
        supportActionBar?.title =
                if (note == null) getString(R.string.new_note)
                else SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.lastChanged)
        initView()
    }

    private fun initView() {
        et_title.removeTextChangedListener(textChangedListener)
        et_body.removeTextChangedListener(textChangedListener)

        note?.let {note ->
            et_title.setText(note.title)
            et_body.setText(note.text)
            val color = when (note.color) {
                Note.Colors.WHITE -> R.color.white
                Note.Colors.YELLOW -> R.color.yellow
                Note.Colors.GREEN -> R.color.green
                Note.Colors.BLUE -> R.color.blue
                Note.Colors.RED -> R.color.red
                Note.Colors.VIOLET -> R.color.violet
            }
            toolbar.setBackgroundColor(resources.getColor(color))
        }
        et_title.addTextChangedListener(textChangedListener)
        et_body.addTextChangedListener(textChangedListener)
    }

    fun saveNote() {
        if (et_title.text == null || et_title.text!!.length < 3) return

        note = note?.copy(
                title = et_title.text.toString(),
                text = et_body.text.toString(),
                lastChanged = Date()
        ) ?: Note(UUID.randomUUID().toString(),
                et_title.text.toString(),
                et_body.text.toString())
        viewModel.save(note!!)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}