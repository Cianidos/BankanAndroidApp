package com.example.bankan.common

import android.app.Application
import com.example.bankan.screens.autheneication.authenticationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class BankanApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BankanApplication)
            modules(authenticationModule)
        }
    }
}