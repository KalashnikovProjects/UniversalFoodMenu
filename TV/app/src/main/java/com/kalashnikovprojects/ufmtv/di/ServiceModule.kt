package com.kalashnikovprojects.ufmtv.di

import com.kalashnikovprojects.ufmtv.data.service.AndroidEventsServiceController
import com.kalashnikovprojects.ufmtv.data.service.AndroidLoginServiceController
import com.kalashnikovprojects.ufmtv.domain.repository.EventsServiceController
import com.kalashnikovprojects.ufmtv.domain.repository.LoginServiceController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    @Singleton
    abstract fun bindLoginServiceController(
        impl: AndroidLoginServiceController
    ): LoginServiceController

    @Binds
    @Singleton
    abstract fun bindEventsServiceController(
        impl: AndroidEventsServiceController
    ): EventsServiceController
}