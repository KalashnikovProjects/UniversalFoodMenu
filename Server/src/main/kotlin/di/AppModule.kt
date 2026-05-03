package com.kalashnikovprojects.ufmserver.di

import com.kalashnikovprojects.ufmserver.adapters.filestorage.FileStorageAdapter
import com.kalashnikovprojects.ufmserver.adapters.hashing.HashingAdapter
import com.kalashnikovprojects.ufmserver.adapters.jwt.JwtAdapter
import com.kalashnikovprojects.ufmserver.adapters.jwt.JwtConfig
import com.kalashnikovprojects.ufmserver.data.DbConfig
import com.kalashnikovprojects.ufmserver.data.repository.UsersRepository
import com.kalashnikovprojects.ufmserver.data.connectDb
import org.koin.dsl.module


fun getAppModule(
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
        FileStorageAdapter()
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
}