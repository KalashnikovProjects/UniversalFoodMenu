package com.kalashnikovprojects.ufmserver.data

import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase

fun connectDb(
    dbConfig: DbConfig,
): R2dbcDatabase = R2dbcDatabase.connect(
    url = "r2dbc:postgresql://${dbConfig.dbHost}/${dbConfig.dbName}}",
    driver = "postgresql",
    user = dbConfig.user,
    password = dbConfig.password,
)