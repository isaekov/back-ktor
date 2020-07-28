package ru.hwru.server.repository.post

interface Repository<T> {
    suspend fun getAll() : List<T>
    suspend fun getById(id : Long) : T?
    suspend fun updateById(id: Long, data: T) : T
    suspend fun add(data: T) : T
    suspend fun deleteById(id: Long)

}