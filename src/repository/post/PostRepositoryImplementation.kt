package ru.hwru.server.repository.post

import entity.DataSource
import entity.Post
import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*

class PostRepositoryImplementation : PostRepository {

    private val mutex = Mutex()
    private val post = mutableListOf<Post>()

    init {
        post.addAll(DataSource.createDataSet())
    }

    @KtorExperimentalAPI
    override suspend fun like(id: Long): Post? {
        val postId = post.indexOfFirst { it.id == id }
        val currentPost = post[postId]
        if (postId > 0) {
            if (currentPost.likeCount > 0) {
                currentPost.likeCount = +1
                if (!currentPost.likeMe) {
                    currentPost.likeMe = true
                }
            }

        } else {
            throw NotFoundException("Нет токого поста")
        }
        return currentPost
    }

    @KtorExperimentalAPI
    override suspend fun dislike(id: Long): Post? {
        val postId = post.indexOfFirst { it.id == id }
        val currentPost = post[postId]
        if (postId > 0) {
            if (currentPost.likeCount > 0) {
                currentPost.likeCount = -1
                if (currentPost.likeMe) {
                    currentPost.likeMe = false
                }
            }

        } else {
            throw NotFoundException("Нет токого поста")
        }
        return currentPost
    }

    @KtorExperimentalAPI
    override suspend fun share(id: Long): Post? {
        val postId = post.indexOfFirst { it.id == id }
        if (postId > 0) {
            if (!post[postId].shareMe) {
                post[postId].shareCount = +1
                post[postId].shareMe = true
            }
        } else {
            throw NotFoundException("Нет токого поста")
        }
        return post[postId]
    }

    @KtorExperimentalAPI
    override suspend fun forwardPost(id: Long, author: String): Post? {
        val source = getId(id) ?: throw NotFoundException("Запись не найдена")
        val newPost = Post(
            id = getLastId() + 1,
            authorName = author,
            createDate = "123412342",
            post = source
        )
        add(newPost);
        return post.last()
    }

    override suspend fun getAll(): List<Post> {
        return post
    }

    override suspend fun getId(id: Long): Post? {
        return post.find { it.id == id }
    }

    @KtorExperimentalAPI
    override suspend fun updateById(id: Long, data: Post): Post {
        return when (val index = post.indexOfFirst { it.id == id }) {
            -1 -> {
                throw NotFoundException("Нет такой записи")
            }
            else -> {
                data.id = id
                val updatePost = data.copy()
                post[index] = updatePost
                updatePost
            }
        }
    }

    override suspend fun add(data: Post): Post {
        data.id = getLastId() + 1
        val newData = data.copy()
        if (post.add(newData)) {
            return newData
        } else {
            throw Throwable("Данные не сохранены")
        }
    }

    override suspend fun deleteById(id: Long) {
        post.removeIf { it.id == id }
    }

    private fun getLastId(): Long {
        return post.maxBy { it.id }?.id ?: 0L
    }
}