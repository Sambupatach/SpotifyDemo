package com.lowes.demoapp.domain.model

import com.google.gson.annotations.SerializedName
/*
 * Actual Album model that will be used in UI
 */
data class Album (

    @SerializedName("artists")
    var artists : List<Artist>? = null,

    @SerializedName("images")
    var images : List<Image>? = null,

    @SerializedName("name")
    var name : String? = null,

) {
    override fun toString(): String {
        return "Album(artists=$artists, images=$images, name=$name)"
    }

}