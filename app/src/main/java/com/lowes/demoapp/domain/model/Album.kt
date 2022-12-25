package com.lowes.demoapp.domain.model

import com.google.gson.annotations.SerializedName

data class Album (
    @SerializedName("album_type")
    var album_type : String? = null,

    @SerializedName("artists")
    var artists : List<Artist>? = null,

    @SerializedName("available_markets")
    var available_markets : List<String>? = null,

    @SerializedName("external_urls")
    var external_urls : Map<String, String>? = null,

    @SerializedName("href")
    var href : String? = null,

    @SerializedName("id")
    var id :String? = null,

    @SerializedName("images")
    var images : List<Image>? = null,

    @SerializedName("name")
    var name : String? = null,

    @SerializedName("release_date")
    var release_date : String? = null,

    @SerializedName("release_date_precision")
    var release_date_precision : String? = null,

    @SerializedName("total_tracks")
    var total_tracks : Int? = null,

    @SerializedName("type")
    var type : String? = null,

    @SerializedName("uri")
    var uri : String? = null

)