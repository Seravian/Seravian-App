package com.seravian.core_navigation.di

import com.seravian.core_navigation.data.repository.NavigationStateRepository
import com.seravian.core_navigation.utils.AppNavigator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val navigationCoreModule = module {
    singleOf(::AppNavigator)
    singleOf(::NavigationStateRepository)
}