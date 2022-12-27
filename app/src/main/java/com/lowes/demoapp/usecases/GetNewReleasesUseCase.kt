package com.lowes.demoapp.usecases

import android.app.Application
import android.util.Log
import com.lowes.demoapp.domain.model.Album
import com.lowes.demoapp.network.service.SpotifyAccounts

private const val TAG = "GetNewReleasesUseCase"
open class GetNewReleasesUseCase(var spotifyAccountService : SpotifyAccounts) {

    open suspend operator fun invoke(application : Application, token : CharSequence) : List<Album>? {
        Log.d(TAG,"invoke getNewReleases")
        return spotifyAccountService.getNewReleases(token as String, application.applicationContext)
    }
}