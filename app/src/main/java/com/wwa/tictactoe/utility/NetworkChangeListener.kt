package com.wwa.tictactoe.utility

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.wwa.tictactoe.R

class NetworkChangeListener : BroadcastReceiver() {


    /**
     * Called when a network connectivity change is detected.
     */
    @SuppressLint("InflateParams")
    override fun onReceive(context: Context?, intent: Intent?) {

        if (!Common.isConnectedInternet(context)){
            val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
            val layoutDialog: View? = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog,null)
            builder.setView(layoutDialog)
            val btnRetry:AppCompatButton = layoutDialog!!.findViewById(R.id.btnRetry)

            val dialog:AlertDialog = builder.create()
            dialog.show()
            dialog.setCancelable(false)

            dialog.window?.setGravity(Gravity.CENTER)

            btnRetry.setOnClickListener {
                dialog.dismiss()
                onReceive(context,intent)
            }
        }
    }
}