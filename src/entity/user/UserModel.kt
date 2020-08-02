package ru.hwru.server.entity.user

import io.ktor.auth.Principal

data class UserModel(
    val id: Long = 0,
    val username: String,
    val password: String
): Principal