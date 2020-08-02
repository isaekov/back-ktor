package ru.hwru.server.repository.user

import ru.hwru.server.entity.user.UserModel

interface UserRepository {
    suspend fun getAll(): List<UserModel>
    suspend fun getById(id: Long): UserModel?
    suspend fun getByIds(ids: Collection<Long>): List<UserModel>
    suspend fun getByUsername(username: String): UserModel?
    suspend fun save(item: UserModel): UserModel
}