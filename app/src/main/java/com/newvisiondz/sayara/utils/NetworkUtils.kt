package com.newvisiondz.sayara.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager


fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
    val nt = cm!!.activeNetworkInfo

    return (nt != null && nt.isConnected)
}
