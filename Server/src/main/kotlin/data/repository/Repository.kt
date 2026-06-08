package com.kalashnikovprojects.ufmserver.data.repository

interface Repository {
    suspend fun createSchema()
}