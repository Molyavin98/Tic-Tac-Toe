package com.wwa.tictactoe.present

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.ViewModelProvider
import com.wwa.tictactoe.viewmodel.SplashScreenViewModel
import com.wwa.tictactoe.databinding.ActivitySplashScreenBinding
import com.wwa.tictactoe.utility.NetworkChangeListener


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var splashScreenViewModel: SplashScreenViewModel
    private val totalProgress = 100
    private var progressBarLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashScreenViewModel = ViewModelProvider(this)[SplashScreenViewModel::class.java]

        splashScreenViewModel.fetchRemoteConfig()
        splashScreenViewModel.fetchLinkFromDatabase()
        splashScreenViewModel.checkIfGameEnabled()

        splashScreenViewModel.progressLiveData.observe(this) { progress ->
            updateProgressBar(progress)
        }

        fillProgressBar()
    }

    /**
     * Updates the progress bar with the given progress value.
     * If the progress reaches or exceeds the total progress and the progress bar is not yet loaded,
     * it sets the progress bar loaded flag and checks if the game is enabled.
     */
    private fun updateProgressBar(progress: Int) {
        binding.progressBar.progress = progress
        if (progress >= totalProgress && !progressBarLoaded) {
            progressBarLoaded = true
            checkGameEnabled()
        }
    }

    /**
     * Fills the progress bar with the total progress value.
     */
    private fun fillProgressBar() {
        splashScreenViewModel.fillProgressBar(totalProgress)
    }

    /**
     * Checks if the game is enabled by observing the game enable live data.
     * If the game enable status is not null, it navigates to the appropriate screen based on the status.
     */
    private fun checkGameEnabled() {
        splashScreenViewModel.getGameEnabledLiveData().observe(this) { isGameEnabled ->
            if (isGameEnabled != null) {
                if (isGameEnabled) {
                    openWebView()
                } else {
                    openGame()
                }
            }
        }
    }

    /**
     * Opens the WebViewActivity with the URL obtained from the link live data.
     */

    private fun openWebView() {
        val intent = Intent(this@SplashScreenActivity, WebViewActivity::class.java)
        intent.putExtra("web_url", splashScreenViewModel.getLinkLiveData().value)
        startActivity(intent)
        finish()
    }

    /**
     * Opens the GameActivity.
     */
    private fun openGame() {
        startActivity(Intent(this@SplashScreenActivity, GameActivity::class.java))
        finish()
    }

}



