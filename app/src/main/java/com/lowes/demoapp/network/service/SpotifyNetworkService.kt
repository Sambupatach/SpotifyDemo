package com.lowes.demoapp.network.service

import com.lowes.demoapp.network.model.AccessTokenDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface SpotifyNetworkService {

    @FormUrlEncoded
    @POST("/api/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType :String,
        @Header("Authorization") token: String
    ) : AccessTokenDto
}