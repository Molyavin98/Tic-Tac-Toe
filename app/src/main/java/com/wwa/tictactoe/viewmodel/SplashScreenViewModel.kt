package com.wwa.tictactoe.viewmodel

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.wwa.tictactoe.model.FirebaseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {

    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
    private val linkLiveData: MutableLiveData<String> = MutableLiveData()
    private val gameEnabledLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val progressLiveData: MutableLiveData<Int> = MutableLiveData()

    private var isGameEnabledChecked = false

    /**
     * Fetches the link from the Firebase database and updates the linkLiveData with the received URL.
     */
    fun fetchLinkFromDatabase() {
        firebaseRepository.getLinkFromDatabase(object : FirebaseRepository.LinkCallback {
            override fun onLinkReceived(url: String?) {
                linkLiveData.postValue(url)
            }
        })
    }

    /**
     * Returns the LiveData for observing the link.
     */
    fun getLinkLiveData(): LiveData<String> {
        return linkLiveData
    }

    /**
     * Checks if the game is enabled by querying the Firebase repository.
     * It avoids duplicate calls by checking the isGameEnabledChecked flag.
     * Updates the gameEnabledLiveData with the result.
     */
    fun checkIfGameEnabled() {
        if (!isGameEnabledChecked) {
            val isGameEnabled = firebaseRepository.isGameEnabled()
            gameEnabledLiveData.postValue(isGameEnabled)
            isGameEnabledChecked = true
        }
    }

    /**
     * Returns the LiveData for observing the game enabled status.
     */
    fun getGameEnabledLiveData(): LiveData<Boolean> {
        return gameEnabledLiveData
    }

    /**
     * Fetches the remote config from Firebase and activates it.
     * It sets the minimum fetch interval and checks if the game is enabled after a successful fetch.
     */
    fun fetchRemoteConfig() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        firebaseRepository.remoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRepository.remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseRepository.remoteConfig.activate().addOnCompleteListener { activationTask ->
                    if (activationTask.isSuccessful) {
                        checkIfGameEnabled()
                    }
                }
            }
        }
    }

    /**
     * Fills the progress bar by incrementing the progress gradually.
     */
    fun fillProgressBar(totalProgress: Int) {
        val animationDuration = 3000L // Animation duration in milliseconds
        val progressIncrement = 1 // Progress increment

        val animator = ValueAnimator.ofInt(0, totalProgress)
        animator.duration = animationDuration
        animator.interpolator = LinearInterpolator()

        animator.addUpdateListener { valueAnimator ->
            val progress = valueAnimator.animatedValue as Int
            progressLiveData.value = progress
        }

        viewModelScope.launch {
            animator.start()

            var currentProgress = 0
            while (currentProgress < totalProgress) {
                currentProgress += progressIncrement
                delay(animationDuration / totalProgress)

            }
            checkIfGameEnabled()
        }
    }
}

