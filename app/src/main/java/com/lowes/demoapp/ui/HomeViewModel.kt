package com.lowes.demoapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lowes.demoapp.network.service.SpotifyAccounts
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"
class HomeViewModel(var app: Application) : AndroidViewModel(app) {
    private val mSpotifyAccountService : SpotifyAccounts by lazy {
        SpotifyAccounts(app.applicationContext)
    }
    init {
        Log.d(TAG,"init()")

    }
    public fun doInit(){
        Log.d(TAG,"doInit")
        viewModelScope.launch {
            Log.d(TAG,"getToken")
            mSpotifyAccountService.getAccessToken()
        }
    }
}