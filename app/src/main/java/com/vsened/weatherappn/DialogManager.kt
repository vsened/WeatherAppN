package com.vsened.weatherappn

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

object DialogManager {
    fun locationSettingsDialog(context: Context, listener: DialogListener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Enabled location?")
        dialog.setMessage("Location disabled! Do you want to enable location?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            listener.onClick(null)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cansel") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun searchByName(context: Context, listener: DialogListener) {
        val builder = AlertDialog.Builder(context)
        val editText = EditText(context)
        builder.setView(editText)
        val dialog = builder.create()
        dialog.setTitle("City:")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            listener.onClick(editText.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cansel") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    interface DialogListener {
        fun onClick(name: String?)
    }
}