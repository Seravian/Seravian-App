package com.seravian.seravianapp

import android.app.Application
import com.greenvenom.core_network.api.di.apiModule
import com.seravian.seravianapp.di.appModule
import com.greenvenom.feat_auth.di.authFeatureModule
import com.greenvenom.core_navigation.di.navigationCoreModule
import com.seravian.feat_home.di.homeModule
import com.greenvenom.feat_onboarding.di.onBoardingFeatureModule
import com.seravian.feat_chat.di.chatModule
import com.seravian.feat_local.di.localFeatureModule
import com.seravian.feat_navigation.di.navigationFeatureModule
import com.seravian.feat_network.di.networkFeatureModule
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
                localFeatureModule,
                networkFeatureModule,
                navigationCoreModule,
                navigationFeatureModule,
                authFeatureModule,
                onBoardingFeatureModule,
                homeModule,
                chatModule
            )
        }
    }
}