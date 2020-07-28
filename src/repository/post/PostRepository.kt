package ru.hwru.server.repository.post

import entity.Post

interface PostRepository  : Repository<Post>{

    suspend fun like(id: Long) : Post
    suspend fun dislike(id: Long) : Post
    suspend fun share(id: Long) : Post
    suspend fun forwardPost(id: Long, author:String) : Post

}