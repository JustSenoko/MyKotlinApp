package com.example.notesapp.common

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatDate(format: String): String = SimpleDateFormat(format, Locale.getDefault()).format(this)