package com.seravian.seravianapp

import android.app.Application
import com.seravian.seravianapp.di.appModule
import com.seravian.seravianapp.features.auth.di.authenticationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SeravianApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SeravianApp)
            androidLogger()

            modules(
                appModule,
                authenticationModule
            )
        }
    }
}