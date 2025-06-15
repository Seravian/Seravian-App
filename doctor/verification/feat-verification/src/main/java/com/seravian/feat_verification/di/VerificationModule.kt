package com.seravian.feat_verification.di

import com.seravian.feat_verification.data.repo.VerificationRepositoryImpl
import com.seravian.feat_verification.data.source.SeravianVerificationDataSource
import com.seravian.feat_verification.domain.repo.VerificationRepository
import com.seravian.feat_verification.domain.source.VerificationDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.module

val verificationModule = module {
    single<VerificationDataSource> {
        SeravianVerificationDataSource(get(named("authorizedClient")))
    }

    single<VerificationRepository> {
        VerificationRepositoryImpl(get())
    }
}