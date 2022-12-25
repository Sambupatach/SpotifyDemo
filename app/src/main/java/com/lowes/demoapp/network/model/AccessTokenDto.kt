package com.lowes.demoapp.network.model

import com.google.gson.annotations.SerializedName

data class AccessTokenDto(
    @SerializedName("access_token")
    var access_token : String? = null,

    @SerializedName("token_type")
    var token_type : String? = null,

    @SerializedName("expires_in")
    var expires_in : Int? = null
)
