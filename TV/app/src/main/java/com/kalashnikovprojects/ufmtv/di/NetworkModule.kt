package com.kalashnikovprojects.ufmtv.di

import com.kalashnikovprojects.ufmtv.data.remote.EventWebSocketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideKtorClient(): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets)
            // Serialization settings
        }
    }

    @Provides
    @Singleton
    fun provideWebSocketService(client: HttpClient): EventWebSocketService {
        return EventWebSocketService(client)
    }
}