package com.example.mymensa.ui.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mymensa.ui.data.DataStorageRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


    val appModule = module {
        single {
            DataStorageRepository(get())
        }
    }