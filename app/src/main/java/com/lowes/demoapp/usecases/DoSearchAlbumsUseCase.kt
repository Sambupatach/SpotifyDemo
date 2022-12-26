package com.lowes.demoapp.usecases

import android.app.Application
import android.util.Log
import com.lowes.demoapp.domain.model.Album
import com.lowes.demoapp.network.service.SpotifyAccounts

private const val TAG = "DoSearchAlbumsUseCase"
class DoSearchAlbumsUseCase(var spotifyAccountService : SpotifyAccounts) {
    suspend operator fun invoke(application : Application, token : String, query : String) : List<Album>? {
        Log.d(TAG,"invoke doSearch")

        return spotifyAccountService.doSearch(query, token, application.applicationContext)
    }
}