package com.lowes.demoapp.network.model

import com.google.gson.annotations.SerializedName

class NewReleasesDto (
    @SerializedName("albums")
    var albums : NewReleasesPageDto? = null
)