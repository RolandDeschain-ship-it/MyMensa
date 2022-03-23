package com.example.mymensa.ui.home



import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.squareup.moshi.JsonClass
import com.example.mymensa.R
import java.net.Inet4Address

@JsonClass(generateAdapter = true)
data class FoodItem(
    val id: Long,
    val category: String,
    val name: String,
    val prices: Prices,
    val notes: MutableList<String>


    )

@JsonClass(generateAdapter = true)
data class Prices(
    val students:Double
    )

