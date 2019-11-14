package com.example.notesapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.notesapp.R
import com.example.notesapp.common.formatDate
import com.example.notesapp.common.getColorInt
import com.example.notesapp.data.Note
import com.example.notesapp.ui.viewmodels.NoteViewModel
import com.example.notesapp.ui.viewstates.NoteViewState
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {
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
    override val model: NoteViewModel by viewModel()
    private var note: Note? = null
    private var color = Note.Colors.WHITE

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
            model.loadNote(it)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note)
        }
        color_picker.onColorClickListener = {
            color = it
            toolbar.setBackgroundColor(color.getColorInt(this))
            saveNote()
        }
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?) = MenuInflater(this).inflate(R.menu.note_menu, menu).let { true }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) {
            finish()
            return
        }
        this.note = data.note
        initView()
    }

    private fun initView() {
        et_title.removeTextChangedListener(textChangedListener)
        et_body.removeTextChangedListener(textChangedListener)

        note?.let { note ->
            et_title.setTextKeepState(note.title)
            et_body.setTextKeepState(note.text)
            color = note.color
            toolbar.setBackgroundColor(color.getColorInt(this))
            supportActionBar?.title = note.run {
                lastChanged.formatDate(DATE_TIME_FORMAT)
            }
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note)
        }
        et_title.addTextChangedListener(textChangedListener)
        et_body.addTextChangedListener(textChangedListener)
    }

    fun saveNote() {
        if (et_title.text == null || et_title.text!!.length < 3) return

        note = note?.copy(
                title = et_title.text.toString(),
                text = et_body.text.toString(),
                lastChanged = Date(),
                color = color
        ) ?: Note(UUID.randomUUID().toString(),
                et_title.text.toString(),
                et_body.text.toString(),
                color)
        model.save(note!!)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun togglePalette() {
        if (color_picker.isOpen) {
            color_picker.close()
        } else {
            color_picker.open()
        }
    }

    private fun deleteNote() {
        alert {
            messageResource = R.string.note_delete_message
            negativeButton(R.string.no) { dialog -> dialog.dismiss() }
            positiveButton(R.string.yes) { model.deleteNote() }
        }.show()
    }
}