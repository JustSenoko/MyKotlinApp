package com.example.mykotlinapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note (
        val id: String,
        val title: String,
        val text: String,
        val color : Colors = Colors.WHITE,
        val lastChanged: Date = Date())
    : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Note
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    enum class Colors {
        WHITE,
        YELLOW,
        GREEN,
        BLUE,
        RED,
        VIOLET
    }
}

