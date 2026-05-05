package com.kalashnikovprojects.ufmserver.di

import com.kalashnikovprojects.ufmserver.adapters.EventBus.EventBus
import com.kalashnikovprojects.ufmserver.adapters.filestorage.FileStorageAdapter
import com.kalashnikovprojects.ufmserver.adapters.hashing.HashingAdapter
import com.kalashnikovprojects.ufmserver.adapters.jwt.JwtAdapter
import com.kalashnikovprojects.ufmserver.adapters.jwt.JwtConfig
import com.kalashnikovprojects.ufmserver.data.DbConfig
import com.kalashnikovprojects.ufmserver.data.repository.UsersRepository
import com.kalashnikovprojects.ufmserver.data.connectDb
import com.kalashnikovprojects.ufmserver.data.repository.CategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.DesignItemsRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsCategoriesRepository
import com.kalashnikovprojects.ufmserver.data.repository.FoodItemsRepository
import com.kalashnikovprojects.ufmserver.data.repository.ImageItemsRepository
import com.kalashnikovprojects.ufmserver.data.repository.ScreensRepository
import com.kalashnikovprojects.ufmserver.data.repository.TextItemsRepository
import org.koin.dsl.module


fun getAppModule(
    host: String,
    dbConfig: DbConfig,
    jwtConfig: JwtConfig,
) = module {
    single {
        jwtConfig
    }
    single {
        dbConfig
    }
    single {
        FileStorageAdapter(host)
    }
    single {
        HashingAdapter()
    }
    single {
        JwtAdapter(get())
    }
    single {
        connectDb(get())
    }
    single {
        UsersRepository(get())
    }
    single {
        ScreensRepository(get())
    }
    single {
        FoodItemsRepository(get())
    }
    single {
        CategoriesRepository(get())
    }
    single {
        FoodItemsCategoriesRepository(get())
    }
    single {
        ImageItemsRepository(get())
    }
    single {
        TextItemsRepository(get())
    }
    single {
        DesignItemsRepository(get())
    }
    single {
        EventBus()
    }
}