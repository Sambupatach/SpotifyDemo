package com.lowes.demoapp.domain.model

import com.google.gson.annotations.SerializedName

data class Image (
    @SerializedName("height")
    var height : Int? = null,

    @SerializedName("url")
    var url : String? = null,

    @SerializedName("width")
    var width : Int? = null
)