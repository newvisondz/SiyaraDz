package com.newvisiondz.sayara.utils

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar


fun displaySnackBar(view: View, meesage: String) {
    val snackbar = Snackbar.make(view, meesage, Snackbar.LENGTH_LONG).setAction("Nice", null)
    snackbar.setActionTextColor(Color.BLUE)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(Color.DKGRAY)
    snackbar.show()
}

