package com.example.ufmcontroller.di

import com.example.ufmcontroller.data.repository.LoginRepositoryImpl
import com.example.ufmcontroller.data.repository.CategoryRepositoryImpl
import com.example.ufmcontroller.data.repository.DesignRepositoryImpl
import com.example.ufmcontroller.data.repository.FoodRepositoryImpl
import com.example.ufmcontroller.data.repository.GetRepositoryImpl
import com.example.ufmcontroller.data.repository.TvScreenRepositoryImpl
import com.example.ufmcontroller.data.service.AndroidEventsServiceController
import com.example.ufmcontroller.domain.repository.CategoryRepository
import com.example.ufmcontroller.domain.repository.DesignRepository
import com.example.ufmcontroller.domain.repository.EventsServiceRepository
import com.example.ufmcontroller.domain.repository.FoodRepository
import com.example.ufmcontroller.domain.repository.GetRepository
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
    abstract fun bindGetRepository(impl: GetRepositoryImpl): GetRepository

    @Binds
    @Singleton
    abstract fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindDesignRepository(impl: DesignRepositoryImpl): DesignRepository

    @Binds
    @Singleton
    abstract fun bindFoodRepository(impl: FoodRepositoryImpl): FoodRepository

    @Binds
    @Singleton
    abstract fun bindTvScreenRepository(impl: TvScreenRepositoryImpl): TvScreenRepository
}
