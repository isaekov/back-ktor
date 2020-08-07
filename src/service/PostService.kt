package ru.hwru.server.service

import entity.Post
import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import ru.hwru.server.repository.post.PostRepositoryImplementation

@KtorExperimentalAPI
class PostService(private val post: PostRepositoryImplementation) {



    suspend fun like(id: Long) :Post {
        return post.like(id)
    }

    suspend fun dislike(id: Long) : Post {
        return post.dislike(id)
    }

    suspend fun share(id: Long) : Post {
       return post.share(id)
    }

    suspend fun forwardPost(id: Long, authorName: String) : Post {
        return post.forwardPost(id, authorName)
    }

    suspend fun getAll(): List<Post> {
        return post.getAll()
    }

    suspend fun getById(id: Long): Post {
        return post.getById(id) ?: throw NotFoundException()
    }

    suspend fun updateById(id: Long, data: Post) : Post {
        return post.updateById(id, data);
    }

    suspend fun add(data: Post) : Post {
        return post.add(data)
    }

    suspend fun delete(id: Long) {
        post.deleteById(id)
    }




}