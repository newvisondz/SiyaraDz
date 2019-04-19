package com.newvisiondz.sayaradz.Utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.View


class MessagesUtils {

    fun displaySnackBar(view: View, meesage: String) {
        val snackbar = Snackbar.make(view, meesage, Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)
    }

    fun dispalyDialogBox(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)
            .setMessage("Do you want to close this application ?")
            .setCancelable(false)
            .setPositiveButton("Proceed") { dialog, id ->
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("AlertDialogExample")
        alert.show()
    }

}