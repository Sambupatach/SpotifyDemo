package com.lowes.demoapp.network.service

import android.content.Context
import android.util.Log
import com.lowes.demoapp.domain.model.Album
import com.lowes.demoapp.network.model.mapper.AlbumDtoMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor


private const val TAG = "SpotifyAccounts"
class SpotifyAccounts(var context : Context) {
    val SPOTIFY_WEB_ACCOUNTS_ENDPOINT = "https://accounts.spotify.com"
    val SPOTIFY_WEB_API_ENDPOINT = "https://api.spotify.com/v1/"
    // TODO : rely on user cred than client cred
    val CLIENT_SECRET = "Basic NzZhNDk1NGJmNmM4NGE0NThlOTMzN2VhYTI5MjVlOTI6MzFlNDJhNGQ3Mjk1NDM1NmI1NzI3NjRmOWM3N2Y4NDg="

    private var accountService : SpotifyNetworkService? = null
    private var apiService : SpotifyNetworkService? = null
    private val loggingClient by lazy {
        getLoggingClient(HttpLoggingInterceptor.Level.BODY)
    }

    /*
     * Spotify service for Account end point - doesn't need token interceptor
     */
    suspend private fun getAccountService(callbackExecutor: Executor): SpotifyNetworkService? {
        Log.d(TAG,"init AccountService")
        val retrofit: Retrofit = Retrofit.Builder()
            .client(loggingClient)
            .callbackExecutor(callbackExecutor)
            .baseUrl(SPOTIFY_WEB_ACCOUNTS_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SpotifyNetworkService::class.java)
    }

    /*
     * Spotify service for api end point - with token interceptor
     */
    suspend private fun getApiService(accessToken : String, callbackExecutor: Executor): SpotifyNetworkService? {
        Log.d(TAG,"init ApiService")

        var tokenCheckingClient = getTokenCheckClient(accessToken, HttpLoggingInterceptor.Level.BODY)
        val retrofit: Retrofit = Retrofit.Builder()
            .client(tokenCheckingClient)
            .callbackExecutor(callbackExecutor)
            .baseUrl(SPOTIFY_WEB_API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SpotifyNetworkService::class.java)
    }
    private fun getLoggingClient(logLevel: Level): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(logLevel)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }
    private fun getTokenCheckClient(accessToken : String, logLevel: Level): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(logLevel)
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                Log.d(TAG,"orig req: ${chain.request().headers()}")
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                val request = requestBuilder.build()
                Log.d(TAG,"end req: ${request.headers()}")
                chain.proceed(request)
            }
            .addInterceptor(interceptor)
            .build()
    }


    suspend fun getAccessToken(context : Context) : String?{
        var accessToken : String? = null
        try {
            accountService ?: getAccountService(context.mainExecutor)?.let { accountService = it }
            var acccessTokenDto =
                accountService!!.getAccessToken("client_credentials", CLIENT_SECRET)
            Log.d(TAG, "Access Token: " + acccessTokenDto?.access_token)
            acccessTokenDto.let {
                it.access_token?.let {
                    accessToken = it
                }
            }
        }catch (exception : Exception){
            Log.e(TAG,"Exception with getAccessToken : ",exception)
        }
        Log.d(TAG,"return $accessToken")
        return accessToken
    }

    suspend fun getNewReleases(accessToken : String, context : Context) : List<Album>?{
        Log.d(TAG,"getNewReleases token:")
        var finalAlbums : List<Album>? = null
        try {
            apiService ?: getApiService(accessToken, context.mainExecutor)?.let {
                apiService = it
            }
            var releases = apiService!!.getNewReleases("application/json")
            Log.d(TAG, "releses: $releases")
            finalAlbums = releases?.albums?.items?.run { AlbumDtoMapper().toDomainList(this) }
        }catch (exception : Exception){
            Log.e(TAG,"Exception with newReleases : ",exception)
        }
        return finalAlbums
    }

    suspend fun doSearch(query : String, accessToken : String, context : Context): List<Album>? {
        Log.d(TAG, "doSearch query:$query")
        var finalAlbums : List<Album>? = null
        try {
            apiService ?: getApiService(accessToken, context.mainExecutor)?.let {
                apiService = it
            }
            var albums = apiService!!.search("application/json", query, "album")
            Log.d(TAG, "albums:$albums")
            finalAlbums = albums?.albums?.items?.run { AlbumDtoMapper().toDomainList(this) }
        }catch (exception : Exception){
            Log.e(TAG,"Exception while searching : ",exception)
        }
        return finalAlbums
    }
}