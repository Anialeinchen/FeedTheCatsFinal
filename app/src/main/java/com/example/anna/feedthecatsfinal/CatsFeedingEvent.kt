package com.example.anna.feedthecatsfinal

import com.google.gson.annotations.SerializedName

data class CatsFeedingEvent(@SerializedName("date") var date: Long,
                            @SerializedName("state")var state: Int,
                            @SerializedName("type")var type: Int)