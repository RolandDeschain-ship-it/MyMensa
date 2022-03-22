package com.example.mymensa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.mymensa.databinding.FragmentCitySelectBinding
import com.example.mymensa.ui.data.DataStorageRepository
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONTokener
import org.koin.android.ext.android.inject
import java.io.IOException

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class CitySelectFragment : Fragment() {
    private var _binding: FragmentCitySelectBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var cityList: ArrayList<String> = ArrayList()
    private val dataStore: DataStorageRepository by inject()


    private var isFullscreen: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCitySelectBinding.inflate(inflater, container, false)


        getCitys()

        val arrayAdapter =
            activity?.let { ArrayAdapter(it.applicationContext, R.layout.dropdown_item, cityList) }
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            lifecycleScope.launch {
                val selectedItem = parent.getItemAtPosition(position).toString()
                storeCity(selectedItem)
                binding.cityNextButton.isEnabled = true
            }
        }

        binding.cityNextButton.isEnabled = false

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cityNextButton.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_citySelectFragment_to_mensaSelect)
        }

    }

    /**
     * Get the json from the url https://openmensa.org/api/v2/canteens with OkHttp
     */
    fun getCitys() {
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("https://openmensa.org/api/v2/canteens")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val tmpCityList = ArrayList<String>()
                if (response.isSuccessful) {
                    val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONArray
                    for (i in 0 until jsonObject.length()) {
                        val city = jsonObject.getJSONObject(i).getString("city")
                        tmpCityList.add(city)
                    }
                } else {
                    tmpCityList.add("Leipzig")
                    tmpCityList.add("Berlin")
                    tmpCityList.add("Bonn")
                }
                cityList.addAll(tmpCityList.distinct())
            }
        })
    }

    suspend fun storeCity(city: String) {
        dataStore.store("city", city)
    }

}