package com.me.downloadfiles.retrofit2.download

import com.google.gson.annotations.SerializedName

data class ConfigureData(
    @SerializedName("data")
    val data: List<Data>
)

data class Data(
    @SerializedName("file_name")
    val fileName: String? = null,
    @SerializedName("type")
    val type: Int? = null
)
