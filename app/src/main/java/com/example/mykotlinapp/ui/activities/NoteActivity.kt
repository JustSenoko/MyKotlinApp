package com.example.mykotlinapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.mykotlinapp.R
import com.example.mykotlinapp.data.Note
import com.example.mykotlinapp.ui.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {
    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.note"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, note: Note? = null) {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, note)
            context.startActivity(intent)
        }
    }

    var note: Note? = null
    lateinit var viewModel: NoteViewModel

    private val textChangedListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            saveNote()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra(EXTRA_NOTE)
        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        supportActionBar?.title = if (note == null) getString(R.string.new_note)
        else SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.lastChanged)

        initView()
    }

    private fun initView() {
        et_title.removeTextChangedListener(textChangedListener)
        et_body.removeTextChangedListener(textChangedListener)

        if (note != null) {
            et_title.setText(note?.title ?: "")
            et_body.setText(note?.text ?: "")
            val color = when (note!!.color) {
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