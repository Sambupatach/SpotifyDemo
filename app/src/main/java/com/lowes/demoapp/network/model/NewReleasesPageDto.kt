package com.lowes.demoapp.network.model

import com.google.gson.annotations.SerializedName

class NewReleasesPageDto (
    @SerializedName("href")
    var href :String? = null,

    @SerializedName("items")
    var items : List<AlbumDto>? = null,

    @SerializedName("limit")
    var limit : Int? = null,

    @SerializedName("next")
    var next : String? = null,

    @SerializedName("offset")
    var offset : Int? = null,

    @SerializedName("previous")
    var previous : String? = null,

    @SerializedName("total")
    var total : Int? = null
)