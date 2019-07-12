package com.newvisiondz.sayara.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import android.content.DialogInterface
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.OnColorSelectedListener


fun displaySnackBar(view: View, meesage: String) {
    val snackbar = Snackbar.make(view, meesage, Snackbar.LENGTH_LONG).setAction("Nice", null)
    snackbar.setActionTextColor(Color.BLUE)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(Color.DKGRAY)
    snackbar.show()
}

fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun dispalyDialogBox(context: Context, titel: String, message: String) {
    val dialogBuilder = AlertDialog.Builder(context)
        .setMessage(message)
        .setTitle(titel)
        .setCancelable(false)
        .setPositiveButton("Proceed") { dialog, id ->
            dialog.dismiss()
        }
        .setNegativeButton("Cancel") { dialog, id ->
            dialog.cancel()
        }
    val alert = dialogBuilder.create()
    alert.show()
}

fun displayeColorPicker(context: Context): String {
    var res = ""
    ColorPickerDialogBuilder
        .with(context)
        .setTitle("Choose color")
        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
        .density(12)
        .setOnColorSelectedListener { selectedColor ->
            Toast.makeText(context, "onColorSelected: 0x ${Integer.toHexString(selectedColor)}", Toast.LENGTH_SHORT)
                .show()
            res = Integer.toHexString(selectedColor)
        }
        .setPositiveButton("ok") { dialog, selectedColor, allColors ->
            //            changeBackgroundColor(selectedColor)
        }
        .setNegativeButton("cancel") { dialog, which -> }
        .build()
        .show()
    return res
}