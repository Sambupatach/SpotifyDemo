package com.lowes.demoapp.domain.model

import com.google.gson.annotations.SerializedName

data class Artist (
    @SerializedName("external_urls")
    var external_urls : Map<String, String>? = null,

    @SerializedName("href")
    var href : String? = null,

    @SerializedName("id")
    var id : String? = null,

    @SerializedName("name")
    var name : String? = null,

    @SerializedName("type")
    var type :String? = null,

    @SerializedName("uri")
    var uri :String? = null
)