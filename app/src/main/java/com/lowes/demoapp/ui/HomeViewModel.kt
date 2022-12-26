package com.lowes.demoapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lowes.demoapp.domain.model.Album
import com.lowes.demoapp.network.service.SpotifyAccounts
import com.lowes.demoapp.usecases.DoSearchAlbumsUseCase
import com.lowes.demoapp.usecases.GetAccessTokenUseCase
import com.lowes.demoapp.usecases.GetNewReleasesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"
class HomeViewModel(var app: Application) : AndroidViewModel(app) {
    private var spotifyAccountService = SpotifyAccounts(app.applicationContext)
    private var getAccessTokenUseCase = GetAccessTokenUseCase(spotifyAccountService)
    private var getNewReleasesUseCase = GetNewReleasesUseCase(spotifyAccountService)
    private var doSearchUseCase = DoSearchAlbumsUseCase(spotifyAccountService)

    private val _accessTokenInitialized = MutableStateFlow<Boolean>(false)
    val accessTokenInitialized: Flow<Boolean> = _accessTokenInitialized

    private val _newReleases = MutableStateFlow<List<Album>>(ArrayList<Album>())
    val newReleases: Flow<List<Album>> = _newReleases

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery : Flow<String> = _searchQuery

    private var accessToken : String? = null
    init {
        Log.d(TAG,"init()")
    }
    public fun getAcessToken(){
        Log.d(TAG,"getAcessToken")
        viewModelScope.launch {
            accessToken ?: getAccessTokenUseCase(app).let { accessToken = it }
            Log.d(TAG,"got token:$accessToken")
            _accessTokenInitialized.value = ((if(accessToken != null) true else false))
        }
    }

    public fun getNewReleases() {
        Log.d(TAG, "getNewReleases : accessToken present: ${accessToken != null}")
        viewModelScope.launch {
            var releases = accessToken?.let { getNewReleasesUseCase(app, it) }
            if(releases != null && releases?.size!! > 0){
                Log.d(TAG,"first album: ${releases?.get(0)}")
                _newReleases.value = releases
            }else{
                Log.e(TAG,"No result !!")
            }
        }
    }

    public fun doSearch(query : String){
        Log.d(TAG,"doSearch: accessToken present: ${accessToken != null} query: $query")
        _searchQuery.value = query
        viewModelScope.launch {
            var releases = accessToken?.let { doSearchUseCase( app, it, query) }
            if(releases != null && releases?.size!! > 0){
                Log.d(TAG,"first album: ${releases?.get(0)}")
                _newReleases.value = releases
            }else{
                Log.e(TAG,"No result !!")
            }
        }
    }
}