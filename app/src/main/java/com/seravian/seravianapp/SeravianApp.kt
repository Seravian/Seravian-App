package com.seravian.seravianapp

import android.app.Application
import com.seravian.core_network.api.di.apiModule
import com.seravian.seravianapp.di.appModule
import com.seravian.feat_auth.di.authModule
import com.seravian.core_navigation.di.navigationCoreModule
import com.seravian.feat_home.di.homeModule
import com.seravian.feat_onboarding.di.onBoardingModule
import com.seravian.feat_tokens.di.tokensModule
import com.seravian.feat_chat.di.chatModule
import com.seravian.feat_doctors.di.doctorModule
import com.seravian.feat_local.di.localModule
import com.seravian.feat_navigation.di.navigationFeatureModule
import com.seravian.feat_network.di.networkModule
import com.seravian.feat_profile.di.profileModule
import com.seravian.feat_verification.di.verificationModule
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
                tokensModule,
                localModule,
                networkModule,
                navigationCoreModule,
                navigationFeatureModule,
                authModule,
                onBoardingModule,
                homeModule,
                chatModule,
                profileModule,
                doctorModule,
                verificationModule
            )
        }
    }
}