package com.seravian.feat_local.di

import androidx.room.Room
import com.seravian.feat_local.data.AppPrefsDataSource
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.core_local.domain.PrefsDataSource
import com.seravian.feat_local.data.RoomDataSource
import com.seravian.feat_local.data.db.SeravianDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            SeravianDatabase::class.java,
            name = "seravian.db"
        ).build()
    }

    single {
        get<SeravianDatabase>().profileDao
    }

    single {
        get<SeravianDatabase>().chatDao
    }

    single<PrefsDataSource> {
        AppPrefsDataSource(androidContext())
    }

    single<LocalDataSource> {
        RoomDataSource(
            profileDao = get(),
            chatDao = get()
        )
    }
}