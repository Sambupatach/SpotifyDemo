package com.lowes.demoapp.usecases

import android.app.Application
import android.util.Log
import com.lowes.demoapp.domain.model.Album
import com.lowes.demoapp.network.service.SpotifyAccounts

private const val TAG = "DoSearchAlbumsUseCase"
open class DoSearchAlbumsUseCase(var spotifyAccountService : SpotifyAccounts) {
    open suspend operator fun invoke(application : Application, token : CharSequence, query : CharSequence) : List<Album>? {
        Log.d(TAG,"invoke doSearch")

        return spotifyAccountService.doSearch(query as String, token as String, application.applicationContext)
    }
}