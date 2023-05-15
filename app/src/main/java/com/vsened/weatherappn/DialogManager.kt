package com.vsened.weatherappn

import android.app.AlertDialog
import android.content.Context

object DialogManager {
    fun locationSettingsDialog(context: Context, listener: DialogListener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Enabled location?")
        dialog.setMessage("Location disabled! Do you want to enable location?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            listener.onClick()
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cansel") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    interface DialogListener {
        fun onClick()
    }
}