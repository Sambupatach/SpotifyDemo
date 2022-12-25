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
    val CLIENT_SECRET = "Basic NzZhNDk1NGJmNmM4NGE0NThlOTMzN2VhYTI5MjVlOTI6MzFlNDJhNGQ3Mjk1NDM1NmI1NzI3NjRmOWM3N2Y4NDg="

    private lateinit var accountService : SpotifyNetworkService
    private lateinit var apiService : SpotifyNetworkService
    private lateinit var accessToken : String
    private val loggingClient by lazy {
        getLoggingClient(HttpLoggingInterceptor.Level.BODY)
    }


    private fun getAccountService(callbackExecutor: Executor): SpotifyNetworkService? {
        Log.d(TAG,"init AccountService")
        val retrofit: Retrofit = Retrofit.Builder()
            .client(loggingClient)
            .callbackExecutor(callbackExecutor)
            .baseUrl(SPOTIFY_WEB_ACCOUNTS_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SpotifyNetworkService::class.java)
    }
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


    suspend public fun getAccessToken(context : Context) : String{
        getAccountService( context.mainExecutor)?.let { accountService = it }
        var acccessTokenDto = accountService.getAccessToken("client_credentials",CLIENT_SECRET)
        Log.d(TAG, "Access Token: "+acccessTokenDto?.access_token)

        acccessTokenDto.let {
            it.access_token?.let {
                accessToken = it
            }
        }
        Log.d(TAG,"return $accessToken")
        return accessToken
    }

    suspend fun getNewReleases(accessToken : String, context : Context) : List<Album>?{
        Log.d(TAG,"getNewReleases token:")
        getApiService(accessToken, context.mainExecutor)?.let {
            apiService = it
        }
        var releases = apiService.getNewReleases("application/json")
        Log.d(TAG,"releses: $releases")
        return releases?.albums?.items?.run { AlbumDtoMapper().toDomainList(this) }
    }
}