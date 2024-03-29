package com.example.bankan.common

import android.app.Application
import com.example.bankan.data.dataModule
import com.example.bankan.screens.screensModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Suppress("unused")
class BankanApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BankanApplication)
            modules(screensModule, dataModule,
                module {
                    viewModelOf(::NavigationViewModel)
                })
        }
    }
}