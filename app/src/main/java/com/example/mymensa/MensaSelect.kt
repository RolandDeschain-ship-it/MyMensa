package com.example.mymensa

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymensa.databinding.FragmentMensaSelectBinding
import com.example.mymensa.ui.data.DataStorageRepository
import com.example.mymensa.ui.data.MensaRecyclerAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.first
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
class MensaSelect : Fragment() {

    private var canteenList = mutableListOf<MensaRecyclerAdapter.Mensa>()
    private var city = "Berlin"

    private lateinit var chipGroup: ChipGroup
    private lateinit var adapter: MensaRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var _binding: FragmentMensaSelectBinding? = null
    private val dataStore: DataStorageRepository by inject()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMensaSelectBinding.inflate(inflater, container, false)

        linearLayoutManager = LinearLayoutManager(context)
        binding.mensaChipsView.layoutManager = linearLayoutManager

        adapter = MensaRecyclerAdapter(canteenList as ArrayList<MensaRecyclerAdapter.Mensa>)
        binding.mensaChipsView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mensaNextButton.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_mensaSelect_to_dietSelect)
        }
    }

    override fun onStart() {
        super.onStart()
        if(canteenList.size == 0) {
            lifecycleScope.launch {
                getCanteens(getCity())
            }
        }
    }

    fun updateCanteens(canteens: List<MensaRecyclerAdapter.Mensa>) {
        activity?.runOnUiThread {
            canteenList.clear()
            canteenList.addAll(canteens)
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * Get the json from the url https://openmensa.org/api/v2/canteens with OkHttp
     */
    fun getCanteens(city: String) {
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("https://openmensa.org/api/v2/canteens")
            .build()



        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val tmpCanteenList = ArrayList<MensaRecyclerAdapter.Mensa>()
                if (response.isSuccessful){
                    val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONArray
                    for (i in 0 until jsonObject.length()) {
                        val canteen = jsonObject.getJSONObject(i)
                        val canteenName = jsonObject.getJSONObject(i).getString("name")
                        if (canteen.getString("city") == city) {
                            tmpCanteenList.add(MensaRecyclerAdapter.Mensa(canteenName, canteen.getString("id")))
                        }
                    }
                }else {
                    tmpCanteenList.add(MensaRecyclerAdapter.Mensa("Mensa am Park","1"))
                    tmpCanteenList.add(MensaRecyclerAdapter.Mensa("Mensa Academica","2"))
                    tmpCanteenList.add(MensaRecyclerAdapter.Mensa("Mensa am Peterssteinweg","3"))
                    tmpCanteenList.add(MensaRecyclerAdapter.Mensa("Mensa am Medizincampus","4"))
                }
                updateCanteens(tmpCanteenList.distinct() as MutableList<MensaRecyclerAdapter.Mensa>)
            }
        })
    }

    suspend fun storeMensa(mensa: String) {
        dataStore.store("mensa", mensa)
    }

    suspend fun getCity(): String {
        return dataStore.get("city") ?: "Berlin"
    }
}