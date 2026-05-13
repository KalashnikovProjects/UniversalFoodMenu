package com.kalashnikovprojects.ufmtv.di

import com.kalashnikovprojects.ufmtv.data.repository.MainRepositoryImpl
import com.kalashnikovprojects.ufmtv.data.repository.LoginRepositoryImpl
import com.kalashnikovprojects.ufmtv.domain.repository.MainRepository
import com.kalashnikovprojects.ufmtv.domain.repository.LoginRepository
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
    abstract fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun bindMainRepository(impl: MainRepositoryImpl): MainRepository
}
