package com.lowes.demoapp.usecases

import android.app.Application
import android.util.Log
import com.lowes.demoapp.network.service.SpotifyAccounts


private const val TAG = "GetAccessTokenUseCase"
class GetAccessTokenUseCase constructor(
){

    suspend operator fun invoke(application : Application) : String{
        Log.d(TAG,"getToken")
        var spotifyAccountService = SpotifyAccounts(application.applicationContext)
        return spotifyAccountService.getAccessToken(application.applicationContext)
    }
}