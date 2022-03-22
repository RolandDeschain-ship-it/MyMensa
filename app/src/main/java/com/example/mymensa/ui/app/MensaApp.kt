package com.example.mymensa.ui.app

import android.app.Application
import com.example.mymensa.ui.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MensaApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MensaApp)
            modules(appModule)
        }
    }
}
