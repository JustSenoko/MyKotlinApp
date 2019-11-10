package com.example.notesapp.ui

import android.app.AlertDialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.notesapp.R


class LogoutDialog : DialogFragment() {

    companion object{
        val TAG = LogoutDialog::class.java.name + "TAG"
        fun createInstance() = LogoutDialog()
    }

    interface LogoutListener {
        fun onLogout()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = AlertDialog.Builder(context)
            .setTitle(getString(R.string.logout_dialog_title))
            .setMessage(getString(R.string.logout_dialog_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> (activity as LogoutListener).onLogout()}
            .setNegativeButton(getString(R.string.no)) { _, _ -> dismiss() }
            .create()
}