package com.lowes.demoapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lowes.demoapp.network.service.SpotifyAccounts
import com.lowes.demoapp.usecases.GetAccessTokenUseCase
import com.lowes.demoapp.usecases.GetNewReleasesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"
class HomeViewModel(var app: Application) : AndroidViewModel(app) {
    private var getAccessTokenUseCase = GetAccessTokenUseCase()
    private var getNewReleasesUseCase = GetNewReleasesUseCase()

    private val _accessTokenInitialized = MutableSharedFlow<Boolean>()
    val accessTokenInitialized: Flow<Boolean> = _accessTokenInitialized

    private lateinit var accessToken : String
    init {
        Log.d(TAG,"init()")
    }
    public fun doInit(){
        Log.d(TAG,"doInit")
        viewModelScope.launch {
            var accessToken = getAccessTokenUseCase(app)
            Log.d(TAG,"got token:$accessToken")
            _accessTokenInitialized.emit((if(accessToken != null) true else false))


        }
    }

    public fun getNewReleases() {
        Log.d(TAG, "getNewReleases")
        viewModelScope.launch {
            var releases = getNewReleasesUseCase(app, accessToken)
        }
    }
}