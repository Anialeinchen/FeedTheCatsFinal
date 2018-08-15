package com.example.anna.feedthecatsfinal

import com.google.gson.annotations.SerializedName

 class CatsFeedingEvent {

     @SerializedName("date") var date: Long = 0
     @SerializedName("state")var state: Int = 0
     @SerializedName("type")var type: Int = 0
     @SerializedName("caregiver_morning")var caregiverMorning: String = ""
     @SerializedName("caregiver_evening")var caregiverEvening: String = ""
 }