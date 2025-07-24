package com.salsa.clone.salsaclone.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(network) ?: return false
    return actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}