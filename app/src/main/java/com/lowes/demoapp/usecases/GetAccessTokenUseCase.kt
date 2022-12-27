package com.lowes.demoapp.usecases

import android.app.Application
import android.util.Log
import com.lowes.demoapp.network.service.SpotifyAccounts


private const val TAG = "GetAccessTokenUseCase"
open class GetAccessTokenUseCase (var spotifyAccountService : SpotifyAccounts){

    open suspend operator fun invoke(application : Application) : String?{
        Log.d(TAG,"getToken")
        return spotifyAccountService.getAccessToken(application.applicationContext)
    }
}