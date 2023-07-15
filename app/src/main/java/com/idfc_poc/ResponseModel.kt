package com.idfc_poc

import com.google.gson.annotations.SerializedName


data class ResponseModel(

    @SerializedName("rc") var rc: String? = null,
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("ts") var ts: String? = null,
    @SerializedName("data") var data: Data? = Data(),

    )

data class Data(

    @SerializedName("keys") var keys: String? = null

)