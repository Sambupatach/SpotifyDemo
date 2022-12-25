package com.lowes.demoapp.network.model

import com.google.gson.annotations.SerializedName

data class ImageDto (
    @SerializedName("height")
    var height : Int? = null,

    @SerializedName("url")
    var url : String? = null,

    @SerializedName("width")
    var width : Int? = null
)