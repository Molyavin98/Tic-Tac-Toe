package com.wwa.tictactoe.utility

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Common {

    /**
     * Checks if the device is connected to the internet.
     */
    companion object {
        fun isConnectedInternet(context: Context?): Boolean {

            val connectivityManager: ConnectivityManager =
                context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

            if (connectivityManager != null) {
                val info = connectivityManager.allNetworkInfo
                if (info != null) {
                    for (i in info.indices) {
                        if (info[i].state == NetworkInfo.State.CONNECTED)
                            return true
                    }
                }
            }
            return false
        }
    }
}