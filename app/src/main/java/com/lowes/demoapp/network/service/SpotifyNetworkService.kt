package com.lowes.demoapp.network.service

import com.lowes.demoapp.network.model.AccessTokenDto
import com.lowes.demoapp.network.model.NewReleasesDto
import retrofit2.http.*

interface SpotifyNetworkService {

    @FormUrlEncoded
    @POST("/api/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType :String,
        @Header("Authorization") token: String
    ) : AccessTokenDto

    @GET("browse/new-releases")
    suspend fun getNewReleases(
        @Header("Content-Type") contentType: String
    ) : NewReleasesDto

    @GET("search")
    suspend fun search(
        @Header("Content-Type") contentType: String,
        @Query("q") query: String,
        @Query("type") type: String
    ) : NewReleasesDto
}