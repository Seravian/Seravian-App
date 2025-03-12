package com.greenvenom.core_navigation.di

import com.greenvenom.core_navigation.data.repository.NavigationStateRepository
import com.greenvenom.core_navigation.utils.AppNavigator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val navigationCoreModule = module {
    singleOf(::AppNavigator)
    singleOf(::NavigationStateRepository)
}