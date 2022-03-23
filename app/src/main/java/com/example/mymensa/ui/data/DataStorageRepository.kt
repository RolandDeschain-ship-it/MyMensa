package com.example.mymensa.ui.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener


class DataStorageRepository(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="settings")


    suspend fun store(key: String, value: String) {
        val keyPreference = stringPreferencesKey(key)
        context.dataStore?.edit { settings ->
            settings[keyPreference] = value
        }
    }

    suspend fun get(key: String): String? {
        val keyPreference = stringPreferencesKey(key)
        return context?.dataStore?.data?.first()?.get(keyPreference)
    }

    suspend fun addMensa(value: MensaRecyclerAdapter.Mensa) {
        val keyPreference = stringPreferencesKey("mensen")
        context.dataStore?.edit { settings ->
            var jsonArray =
                JSONTokener(settings[keyPreference] ?: "[]").nextValue() as JSONArray

            var exists = false
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.getString("id") == value.id) {
                    exists = true
                    break
                }
            }

            if (!exists) {
                var newJsonObject = JSONObject().put("name", value.name).put("id", value.id)
                jsonArray.put(newJsonObject)
                settings[keyPreference] = jsonArray.toString()
            }

            println(settings[keyPreference])

        }
    }

    suspend fun mensaExists(value: MensaRecyclerAdapter.Mensa): Boolean {
        val keyPreference = stringPreferencesKey("mensen")
        var exists = false
        context.dataStore?.edit { settings ->
            var jsonArray =
                JSONTokener(settings.get(keyPreference)?: "[]").nextValue() as JSONArray

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.getString("id") == value.id) {
                    exists = true
                }
            }

            exists = false
        }
        return exists
    }

    suspend fun mensaCount(): Int {
        val keyPreference = stringPreferencesKey("mensen")
        var count = 0
        context.dataStore?.edit { settings ->
            var jsonArray =
                JSONTokener(settings[keyPreference] ?: "[]").nextValue() as JSONArray

            count = jsonArray.length()
        }
        return count
    }

    suspend fun removeMensa(mensa: MensaRecyclerAdapter.Mensa) {
        val keyPreference = stringPreferencesKey("mensen")
        context.dataStore?.edit { settings ->
            var jsonArray =
                JSONTokener(settings.get(keyPreference)?: "[]").nextValue() as JSONArray

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                if (jsonObject.getString("id") == mensa.id) {
                    jsonArray.remove(i)
                    break
                }
            }

            settings[keyPreference] = jsonArray.toString()
        }
    }

    suspend fun getMensen(): List<MensaRecyclerAdapter.Mensa> {
        val keyPreference = stringPreferencesKey("mensen")
        var mensen = mutableListOf<MensaRecyclerAdapter.Mensa>()
        context.dataStore?.edit { settings ->
            var jsonArray =
                JSONTokener(settings[keyPreference] ?: "[]").nextValue() as JSONArray

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                mensen.add(MensaRecyclerAdapter.Mensa(jsonObject.getString("name"), jsonObject.getString("id")))
            }
        }
        return mensen
        }

    suspend fun addToArray(key: String, value: String) {
        val keyPreference = stringPreferencesKey(key)
        context.dataStore?.edit { settings ->
            var jsonArray =
                JSONTokener(settings[keyPreference] ?: "[]").nextValue() as JSONArray

            var exists = false
            for (i in 0 until jsonArray.length()) {
                val arrayValue = jsonArray.getString(i)
                if (arrayValue == value) {
                    exists = true
                    break
                }
            }

            if (!exists) {
                jsonArray.put(value)
                settings[keyPreference] = jsonArray.toString()
            }

            println(settings[keyPreference])
        }
    }

    suspend fun removeFromArray(key: String, value: String) {
        val keyPreference = stringPreferencesKey(key)
        context.dataStore?.edit { settings ->
            var jsonArray =
                JSONTokener(settings[keyPreference] ?: "[]").nextValue() as JSONArray

            for (i in 0 until jsonArray.length()) {
                val arrayValue = jsonArray.getString(i)
                if (arrayValue == value) {
                    jsonArray.remove(i)
                    break
                }
            }

            settings[keyPreference] = jsonArray.toString()
        }
    }

    suspend fun getStringArray(key: String): List<String> {
        val keyPreference = stringPreferencesKey(key)
        var list = mutableListOf<String>()
        context.dataStore?.edit { settings ->
            var jsonArray =
                JSONTokener(settings[keyPreference] ?: "[]").nextValue() as JSONArray

            for (i in 0 until jsonArray.length()) {
                val arrayValue = jsonArray.getString(i)
                list.add(arrayValue)
            }
        }
        return list
    }

    suspend fun isSetup(): Boolean {
        val keyPreferenceMensa = stringPreferencesKey("mensen")
        val keyPreferenceDiet = stringPreferencesKey("diets")
        var setup = false
        context.dataStore?.edit { settings ->
            setup = settings.contains(keyPreferenceMensa)
                    && settings.contains(keyPreferenceDiet)
        }
        println("Setup: $setup")
        return setup
    }

    suspend fun clearAll(){
        val keyPreferenceCity = stringPreferencesKey("city")
        val keyPreferenceMensa = stringPreferencesKey("mensen")
        val keyPreferenceDiet = stringPreferencesKey("diets")
        context.dataStore?.edit { settings ->
            settings.remove(keyPreferenceCity)
            settings.remove(keyPreferenceMensa)
            settings.remove(keyPreferenceDiet)
        }
    }

}