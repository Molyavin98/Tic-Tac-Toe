package com.wwa.tictactoe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebViewViewModel : ViewModel() {

    private val urlLiveData: MutableLiveData<String> = MutableLiveData()

    /**
     * Returns the LiveData for observing the URL to be loaded in the WebView.
     */
    fun getUrlLiveData(): LiveData<String> {
        return urlLiveData
    }

    /**
     * Sets the URL value in the urlLiveData.
     * This URL will be loaded in the WebView.
     */
    fun setUrl(url: String) {
        urlLiveData.value = url
    }
}