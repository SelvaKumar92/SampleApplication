package com.tvs.sample.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TableData {

    @SerializedName("TABLE_DATA")
    @Expose
    var tableData: String? = null
}