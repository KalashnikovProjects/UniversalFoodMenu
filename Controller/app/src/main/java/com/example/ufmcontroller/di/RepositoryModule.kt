package com.example.ufmcontroller.di

import com.example.ufmcontroller.data.repository.LoginRepositoryImpl
import com.example.ufmcontroller.data.repository.MainRepositoryImpl
import com.example.ufmcontroller.data.service.AndroidEventsServiceController
import com.example.ufmcontroller.domain.repository.CategoryRepository
import com.example.ufmcontroller.domain.repository.DesignRepository
import com.example.ufmcontroller.domain.repository.EventsServiceRepository
import com.example.ufmcontroller.domain.repository.FoodRepository
import com.example.ufmcontroller.domain.repository.LoginRepository
import com.example.ufmcontroller.domain.repository.TvScreenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindEventsServiceRepository(impl: AndroidEventsServiceController): EventsServiceRepository

    @Binds
    @Singleton
    abstract fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: MainRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindDesignRepository(impl: MainRepositoryImpl): DesignRepository

    @Binds
    @Singleton
    abstract fun bindFoodRepository(impl: MainRepositoryImpl): FoodRepository

    @Binds
    @Singleton
    abstract fun bindTvScreenRepository(impl: MainRepositoryImpl): TvScreenRepository
}
