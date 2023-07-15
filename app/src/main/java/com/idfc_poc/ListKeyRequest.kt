package com.idfc_poc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ListKeyRequest(

    @SerializedName("device")  var device: Device? = Device(),
    @SerializedName("user")  var user: User? = User(),
    @SerializedName("payload")  var payload: Payload? = Payload(),
    @SerializedName("seq-no")  var seq_no: String? = null

)

data class Device (

    @SerializedName("app-id"      )  var app_id      : String? = null,
    @SerializedName("device-id"   )  var device_id   : String? = null,
    @SerializedName("os"          )  var os          : String? = null,
    @SerializedName("os-version"  )  var os_version  : String? = null,
    @SerializedName("manufacture" )  var manufacture : String? = null,
    @SerializedName("model"       )  var model       : String? = null,
    @SerializedName("version"     )  var version     : String? = null,
    @SerializedName("location"    )  var location    : String? = null,
    @SerializedName("fcm-token"   )  var fcm_token   : String? = null,
    @SerializedName("sim-slot"    )  var sim_slot    : String? = null

)

data class User (

    @SerializedName("abc-profile-id" )  var abc_profile_id : Int?    = null,
    @SerializedName("mobile"         )  var mobile         : String? = null

)
data class Payload (

    @SerializedName("psp" )  var psp : String? = null

)