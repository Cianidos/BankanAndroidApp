@file:Suppress("USELESS_CAST")

package com.example.bankan.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.bankan.data.repository.*
import com.example.bankan.data.store.room.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.preferences: DataStore<Preferences> by preferencesDataStore(name = "profile")

val dataModule = module {
    single { get<Context>().preferences as DataStore<Preferences> }
    single { LocalBoardInfoRepositoryImpl() as LocalBoardInfoRepositoryImpl }
    single { LocalBoardInfoRepositoryImpl() as BoardInfoRepository }
    single { ListInfoRepositoryImpl() as ListInfoRepository }
    single { CardInfoRepositoryImpl() as CardInfoRepository }

    single { ProfileRepositoryInDataStoreNoInternetImpl() as ProfileRepository }

    single(createdAtStart = true) {
        Room.databaseBuilder(
            androidContext(),
            AppDataBase::class.java,
            "main_database"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppDataBase>().boardInfoDao() }
    single { get<AppDataBase>().listInfoDao() }
    single { get<AppDataBase>().cardInfoDao() }
}
