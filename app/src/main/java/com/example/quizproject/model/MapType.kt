package com.example.quizproject.model
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MapType(
    val map: Map<String, Int>
) : Parcelable
