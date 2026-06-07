package com.kalashnikovprojects.ufmtv.di

import android.content.Context
import android.util.Log
import com.kalashnikovprojects.ufmtv.BuildConfig
import com.kalashnikovprojects.ufmtv.data.local.UserPreferencesDataSource
import com.kalashnikovprojects.ufmtv.data.remote.EventsWebSocketService
import com.kalashnikovprojects.ufmtv.data.remote.LoginWebSocketService
import com.kalashnikovprojects.ufmtv.data.remote.NoTokenRequest
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
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
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
                    encodeDefaults = true
                    classDiscriminator = "type"
                    ignoreUnknownKeys = true
                })
            }
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
    fun provideLoginWebSocketService(@ApplicationContext context: Context, client: HttpClient): LoginWebSocketService {
        return LoginWebSocketService(context, client)
    }
}