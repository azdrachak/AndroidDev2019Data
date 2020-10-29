package com.example.androiddata.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Monster (
    @Json(name = "monsterName") val name: String,
    val imageFile: String,
    val caption: String,
    val description: String,
    val price: Double,
    val scariness: Int
)
