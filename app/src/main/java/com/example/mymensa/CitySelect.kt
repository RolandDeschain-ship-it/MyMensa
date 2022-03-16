package com.example.mymensa

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.webkit.JsPromptResult
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mymensa.databinding.ActivityCitySelectBinding
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONTokener
import java.io.IOException

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class CitySelect : AppCompatActivity() {

    private lateinit var binding: ActivityCitySelectBinding
    private lateinit var fullscreenContent: TextView
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler()

    private var isFullscreen: Boolean = false


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        val testText = arrayOf("Leipzig", "Hamburg", "Berlin")
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, testText)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        getCanteens()

    }

    /**
     * Get the json from the url https://openmensa.org/api/v2/canteens with OkHttp
     */
    fun getCanteens() {
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("https://openmensa.org/api/v2/canteens")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                println("Response: ${response.body()?.string()}")
                val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONArray
                println(jsonObject.getJSONObject(0).getString("name"))
            }
        })
        }

    data class MensaOnlyCity(
      val city: String,
    )

    }


