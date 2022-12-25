package com.lowes.demoapp.usecases

import android.app.Application
import android.util.Log
import com.lowes.demoapp.domain.model.Album
import com.lowes.demoapp.network.service.SpotifyAccounts

private const val TAG = "GetNewReleasesUseCase"
class GetNewReleasesUseCase {

    suspend operator fun invoke(application : Application, token : String) : List<Album>? {
        Log.d(TAG,"getToken")
        var spotifyAccountService = SpotifyAccounts(application.applicationContext)
        return spotifyAccountService.getNewReleases(token, application.applicationContext)
    }
}