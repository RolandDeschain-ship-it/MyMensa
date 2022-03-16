package com.example.mymensa

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.mymensa.databinding.ActivityCitySelectBinding
import kotlinx.coroutines.launch
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
    private var cityList: ArrayList<String> = ArrayList()
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

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

        getCanteens()

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, cityList)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            lifecycleScope.launch {
                val selectedItem = parent.getItemAtPosition(position).toString()
                storeCity(selectedItem)
                binding.cityNextButton.isEnabled = true
            }
        }

        binding.cityNextButton.isEnabled = false
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
                val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONArray
                val tmpCityList = ArrayList<String>()
                for (i in 0 until jsonObject.length()) {
                    val city = jsonObject.getJSONObject(i).getString("city")
                    tmpCityList.add(city)
                }
                cityList.addAll(tmpCityList.distinct())
            }
        })
        }

    suspend fun storeCity(city: String) {
        val key = stringPreferencesKey("city")
        dataStore.edit { settings ->
            settings[key] = city
        }
    }


    }


