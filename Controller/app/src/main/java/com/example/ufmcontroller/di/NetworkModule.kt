package com.example.ufmcontroller.di

import android.content.Context
import android.util.Log
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
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideKtorClient(dataStore: UserPreferencesDataSource): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets)
            install(Auth) {
                bearer {
                    sendWithoutRequest { request ->
                        !request.attributes.contains(NoTokenRequest)
                    }

                    loadTokens {
                        Log.d("UFM", "load_tokens")

                        val token = dataStore.authToken.value

                        if (!token.isNullOrBlank()) {
                            BearerTokens(accessToken = token, refreshToken = "")
                        } else {
                            null
                        }
                    }
                    refreshTokens { null }
                }
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY // TODO: HEADERS
//                sanitizeHeader { header ->
//                    header.equals(HttpHeaders.Authorization, ignoreCase = true)
//                }
            }

            defaultRequest {
                url {
                    url("http://${BuildConfig.SERVER_HOST}/") // TODO: URLProtocol.HTTPS
                }
                contentType(ContentType.Application.Json)
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
    fun provideRemoteFoodDataSource(client: HttpClient, @ApplicationContext context: Context): RemoteFoodDataSource {
        return RemoteFoodDataSource(client, context)
    }

    @Provides
    @Singleton
    fun provideRemoteCategoryDataSource(client: HttpClient, @ApplicationContext context: Context): RemoteCategoryDataSource {
        return RemoteCategoryDataSource(client, context)
    }

    @Provides
    @Singleton
    fun provideRemoteDesignDataSource(client: HttpClient, @ApplicationContext context: Context): RemoteDesignDataSource {
        return RemoteDesignDataSource(client, context)
    }

    @Provides
    @Singleton
    fun provideRemoteTvScreenyDataSource(client: HttpClient): RemoteTvScreenDataSource {
        return RemoteTvScreenDataSource(client)
    }

    @Provides
    @Singleton
    fun provideRemoteLoginDataSource(client: HttpClient): RemoteLoginDataSource {
        return RemoteLoginDataSource(client)
    }
}