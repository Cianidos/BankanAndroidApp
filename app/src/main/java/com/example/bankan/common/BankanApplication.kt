package com.example.bankan.common

import android.app.Application
import com.example.bankan.data.dataModule
import com.example.bankan.screens.autheneication.authenticationModule
import com.example.bankan.screens.main.mainMenuModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class BankanApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BankanApplication)
            modules(authenticationModule, mainMenuModule, dataModule,
                module {
                    viewModel { NavigationViewModel() }
                })
        }
    }
}