package com.tvs.sample.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("data")
    @Expose
    var data: List<List<String>>? = null
}
