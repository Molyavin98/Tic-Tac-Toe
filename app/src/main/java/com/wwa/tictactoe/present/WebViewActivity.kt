package com.wwa.tictactoe.present

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wwa.tictactoe.R
import com.wwa.tictactoe.utility.NetworkChangeListener
import com.wwa.tictactoe.viewmodel.WebViewViewModel

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var webViewViewModel: WebViewViewModel
    private val networkChangeListener: NetworkChangeListener = NetworkChangeListener()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()

        webViewViewModel = ViewModelProvider(this)[WebViewViewModel::class.java]

        // Observe changes in the URL LiveData and load the URL in the WebView
        webViewViewModel.getUrlLiveData().observe(this) { url ->
            url?.let { webView.loadUrl(url) }
        }

        // Get the web URL from the intent and set it in the WebViewViewModel
        val webUrl = intent.getStringExtra("web_url")
        webUrl?.let { webViewViewModel.setUrl(webUrl) }

        // Enable JavaScript and image loading in the WebView settings
        webView.settings.javaScriptEnabled = true
        webView.settings.loadsImagesAutomatically = true
    }


    /**
     * Called when the activity is becoming visible to the user.
     * Registers a network change receiver to listen for network connectivity changes.
     */
    override fun onStart() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener, filter)
        super.onStart()
    }

    /**
     * Called when the activity is no longer visible to the user.
     * Unregisters the network change receiver.
     */
    override fun onStop() {
        unregisterReceiver(networkChangeListener)
        super.onStop()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack()
        }
    }
}