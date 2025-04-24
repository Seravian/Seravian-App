package com.seravian.feat_local.di

import androidx.room.Room
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.core_local.domain.LocalTokenDataSource
import com.seravian.feat_local.data.EncryptedTokenDataSource
import com.seravian.feat_local.data.RoomDataSource
import com.seravian.feat_local.data.db.SeravianDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localFeatureModule = module {
    single {
        get<SeravianDatabase>().profileDao
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            SeravianDatabase::class.java,
            name = "seravian.db"
        ).build()
    }

    single<LocalTokenDataSource> {
        EncryptedTokenDataSource(context = androidContext())
    }

    single<LocalDataSource> {
        RoomDataSource(
            profileDao = get()
        )
    }
}