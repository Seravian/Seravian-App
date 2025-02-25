package com.seravian.seravianapp

import android.app.Application
import com.greenvenom.networking.api.di.apiModule
import com.seravian.seravianapp.di.appModule
import com.greenvenom.auth.di.authenticationModule
import com.greenvenom.navigation.di.navigationModule
import com.seravian.home.di.homeModule
import com.seravian.onboarding.di.onBoardingModule
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
                apiModule,
                navigationModule,
                authenticationModule,
                onBoardingModule,
                homeModule
            )
        }
    }
}