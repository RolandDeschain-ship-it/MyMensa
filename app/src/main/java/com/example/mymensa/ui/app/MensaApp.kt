package com.example.mymensa.ui.app



import android.app.Application
import com.example.mymensa.ui.di.appModule
import org.koin.core.context.startKoin

class MensaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
        }
    }
}