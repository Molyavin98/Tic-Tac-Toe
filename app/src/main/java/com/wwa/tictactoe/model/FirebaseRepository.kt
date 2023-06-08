package com.wwa.tictactoe.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig


class FirebaseRepository {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    /**
     * Callback interface for receiving the link from the database.
     */
    interface LinkCallback {
        fun onLinkReceived(url: String?)
    }

    /**
     * Retrieves the link from the Firebase Realtime Database.
     */
    fun getLinkFromDatabase(callback: LinkCallback) {
        databaseReference.child("web_url").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val url = dataSnapshot.value as? String
                callback.onLinkReceived(url)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onLinkReceived(null)
            }
        })
    }

    /**
     * Checks if the game is enabled based on the Remote Config value.
     */
    fun isGameEnabled(): Boolean {
        return remoteConfig.getBoolean("game_enabled")
    }

}
