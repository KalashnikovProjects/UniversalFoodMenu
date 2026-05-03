package com.kalashnikovprojects.ufmserver.adapters.hashing

import org.mindrot.jbcrypt.BCrypt

class HashingAdapter{
    suspend fun hashPassword(plainTextPassword: String): String {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt())
    }
    suspend fun checkPassword(plainTextPassword: String, hashed: String): Boolean {
        return BCrypt.checkpw(plainTextPassword, hashed)
    }
}