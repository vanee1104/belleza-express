package com.example.myapplicationarturocashfaster

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActivityItem(
    val icon: Int,
    val title: String,
    val description: String,
    val time: String,
    val type: ActivityType
) : Parcelable

enum class ActivityType {
    BOOKING, PROFILE, SYSTEM, PAYMENT
}