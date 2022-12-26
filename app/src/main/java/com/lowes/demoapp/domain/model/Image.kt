package com.lowes.demoapp.domain.model

import com.google.gson.annotations.SerializedName

/*
 * Actual Image model that will be used in UI
 */
data class Image (
    @SerializedName("height")
    var height : Int? = null,

    @SerializedName("url")
    var url : String? = null,

    @SerializedName("width")
    var width : Int? = null
) {
    override fun toString(): String {
        return "Image(height=$height, url=$url, width=$width)"
    }
}