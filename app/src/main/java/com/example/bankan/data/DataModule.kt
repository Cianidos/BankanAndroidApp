package com.example.bankan.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.bankan.data.repository.BoardInfoRepository
import com.example.bankan.data.repository.LocalBoardInfoRepositoryImpl
import com.example.bankan.data.repository.ProfileRepository
import com.example.bankan.data.repository.ProfileRepositoryInDataStoreNoInternetImpl
import com.example.bankan.data.store.room.AppDataBase
import org.koin.dsl.module

private val Context.preferences: DataStore<Preferences> by preferencesDataStore(name = "profile")

val dataModule = module {
    single { get<Context>().preferences as DataStore<Preferences> }
    single { LocalBoardInfoRepositoryImpl() as BoardInfoRepository }
    single { ProfileRepositoryInDataStoreNoInternetImpl() as ProfileRepository }

    single(createdAtStart = true) {
        Room.databaseBuilder(get(), AppDataBase::class.java, "mysuperdb").build()
    }
    single { get<AppDataBase>().boardInfoDao() }
}
