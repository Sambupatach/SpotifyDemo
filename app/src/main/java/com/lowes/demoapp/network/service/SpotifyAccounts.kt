package com.lowes.demoapp.network.service

import android.content.Context
import android.util.Log
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

    private lateinit var mAccountService : SpotifyNetworkService
    private lateinit var mApiService : SpotifyNetworkService
    private lateinit var mAccessToken : String
    private val mLoggingClient by lazy {
        getLoggingClient(HttpLoggingInterceptor.Level.BODY)
    }
    private val mTokenCheckingClient by lazy {
        getTokenCheckClient(mAccessToken, HttpLoggingInterceptor.Level.BODY)
    }

    init {
        getAccountService( context.mainExecutor)?.let { mAccountService = it }
    }
    private fun getAccountService(callbackExecutor: Executor): SpotifyNetworkService? {
        Log.d(TAG,"init AccountService")
        val retrofit: Retrofit = Retrofit.Builder()
            .client(mLoggingClient)
            .callbackExecutor(callbackExecutor)
            .baseUrl(SPOTIFY_WEB_ACCOUNTS_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SpotifyNetworkService::class.java)
    }
    suspend private fun getApiService(accessToken : String, callbackExecutor: Executor): SpotifyNetworkService? {
        Log.d(TAG,"init ApiService")
        val retrofit: Retrofit = Retrofit.Builder()
            .client(mTokenCheckingClient)
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
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(interceptor)
            .build()
    }
    suspend public fun getAccessToken() : String{
        var acccessTokenDto = mAccountService.getAccessToken("client_credentials",CLIENT_SECRET)
        Log.d(TAG, "Access Token: "+acccessTokenDto?.access_token)

        acccessTokenDto.let {
            it.access_token?.let {
                mAccessToken = it
                getApiService(it, context.mainExecutor)?.let {
                    mApiService = it
                }
            }

        }
        Log.d(TAG,"return $mAccessToken")
        return mAccessToken
    }
}