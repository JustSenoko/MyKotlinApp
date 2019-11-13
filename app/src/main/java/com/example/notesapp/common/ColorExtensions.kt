package com.example.notesapp.common

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.notesapp.R
import com.example.notesapp.data.Note

fun Note.Colors.getColorInt(context: Context): Int = ContextCompat.getColor(
        context, when (this) {
    Note.Colors.WHITE -> R.color.white
    Note.Colors.YELLOW -> R.color.yellow
    Note.Colors.GREEN -> R.color.green
    Note.Colors.BLUE -> R.color.blue
    Note.Colors.RED -> R.color.red
    Note.Colors.VIOLET -> R.color.violet
}
)