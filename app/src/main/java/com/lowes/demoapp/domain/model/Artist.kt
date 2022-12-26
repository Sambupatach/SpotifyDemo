package com.lowes.demoapp.domain.model

import com.google.gson.annotations.SerializedName

/*
 * Actual Artist model that will be used in UI
 */
data class Artist (

    @SerializedName("name")
    var name : String? = null,

) {
    override fun toString(): String {
        return "Artist(name=$name)"
    }

}