package com.example.ufmcontroller.di

import android.content.Context
import com.example.ufmcontroller.BuildConfig
import com.example.ufmcontroller.data.local.UserPreferencesDataSource
import com.example.ufmcontroller.data.remote.EventsWebSocketService
import com.example.ufmcontroller.data.remote.NoTokenRequest
import com.example.ufmcontroller.data.remote.RemoteCategoryDataSource
import com.example.ufmcontroller.data.remote.RemoteDesignDataSource
import com.example.ufmcontroller.data.remote.RemoteFoodDataSource
import com.example.ufmcontroller.data.remote.RemoteLoginDataSource
import com.example.ufmcontroller.data.remote.RemoteTvScreenDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideKtorClient(dataStore: UserPreferencesDataSource): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
            install(WebSockets)
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.HEADERS
//                sanitizeHeader { header ->
//                    header.equals(HttpHeaders.Authorization, ignoreCase = true)
//                }
            }

            defaultRequest {
                url {
                    url("https://${BuildConfig.SERVER_HOST}/")
                }
                contentType(ContentType.Application.Json)
            }
        }.also {
            it.requestPipeline.intercept(HttpRequestPipeline.State) {
                if (context.attributes.contains(NoTokenRequest)) return@intercept

                val token = dataStore.authToken.firstOrNull()

                if (!token.isNullOrBlank()) {
                    context.header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideEventsWebSocketService(client: HttpClient, dataStore: UserPreferencesDataSource): EventsWebSocketService {
        return EventsWebSocketService(client, dataStore)
    }

    @Provides
    @Singleton
    fun provideRemoteFoodDataSource(client: HttpClient, @ApplicationContext context: Context, scope: CoroutineScope): RemoteFoodDataSource {
        return RemoteFoodDataSource(client, context, scope)
    }

    @Provides
    @Singleton
    fun provideRemoteCategoryDataSource(client: HttpClient, @ApplicationContext context: Context, scope: CoroutineScope): RemoteCategoryDataSource {
        return RemoteCategoryDataSource(client, context, scope)
    }

    @Provides
    @Singleton
    fun provideRemoteDesignDataSource(client: HttpClient, @ApplicationContext context: Context, scope: CoroutineScope): RemoteDesignDataSource {
        return RemoteDesignDataSource(client, context, scope)
    }

    @Provides
    @Singleton
    fun provideRemoteTvScreenyDataSource(client: HttpClient, scope: CoroutineScope): RemoteTvScreenDataSource {
        return RemoteTvScreenDataSource(client,scope)
    }

    @Provides
    @Singleton
    fun provideRemoteLoginDataSource(client: HttpClient, scope: CoroutineScope): RemoteLoginDataSource {
        return RemoteLoginDataSource(client,scope)
    }
}