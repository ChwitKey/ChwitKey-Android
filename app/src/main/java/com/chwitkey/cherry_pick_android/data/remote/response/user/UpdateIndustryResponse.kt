package com.chwitkey.cherry_pick_android.data.remote.response.user


import com.squareup.moshi.Json

data class UpdateIndustryResponse(
    @Json(name = "statusCode")
    val statusCode: Int?
)