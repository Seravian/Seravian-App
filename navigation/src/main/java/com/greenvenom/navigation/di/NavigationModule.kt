package com.greenvenom.navigation.di

import com.greenvenom.navigation.repository.NavigationStateRepository
import com.greenvenom.navigation.utils.AppNavigator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val navigationModule = module {
    singleOf(::AppNavigator)
    singleOf(::NavigationStateRepository)
}