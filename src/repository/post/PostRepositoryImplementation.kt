package ru.hwru.server.repository.post

import entity.DataSource
import entity.Post
import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PostRepositoryImplementation : PostRepository {

    private val mutex = Mutex()
    private val post = mutableListOf<Post>()

    init {
        post.addAll(DataSource.createDataSet())
    }

    @KtorExperimentalAPI
    override suspend fun like(id: Long): Post? {
        mutex.withLock {
            val postId  = post.indexOfFirst { it.id == id }
            val currentPost = post[postId]
            if (postId > 0) {
                if (currentPost.likeCount > 0) {
                    currentPost.likeCount =+ 1
                    if (!currentPost.likeMe) {
                        currentPost.likeMe = true
                    }
                }

            } else {
                throw NotFoundException("Нет токого поста")
            }
            return currentPost
        }
    }

    @KtorExperimentalAPI
    override suspend fun dislike(id: Long): Post? {
        mutex.withLock {
            val postId  = post.indexOfFirst { it.id == id }
            val currentPost = post[postId]
            if (postId > 0) {
                if (currentPost.likeCount > 0) {
                    currentPost.likeCount =- 1
                    if (currentPost.likeMe) {
                        currentPost.likeMe = false
                    }
                }

            } else {
                throw NotFoundException("Нет токого поста")
            }
            return currentPost
        }
    }

    @KtorExperimentalAPI
    override suspend fun share(id: Long): Post? {
        mutex.withLock {
            val postId  = post.indexOfFirst { it.id == id }
            if (postId > 0) {
               if (!post[postId].shareMe) {
                   post[postId].shareCount =+ 1
                   post[postId].shareMe = true
               }
            } else {
                throw NotFoundException("Нет токого поста")
            }
            return post[postId]
        }
    }

    override suspend fun forwardPost(data: Post): Post? {
        mutex.withLock {
            var result: Post? = null
            with(data) {
                val properSourcePost = post?.id?.let { getId(it) }
                if (post != null && properSourcePost != null) {
                    id = -1
                    post = properSourcePost
                    result = add(this)
                }
            }
            return result
        }
    }

    override suspend fun getAll(): List<Post> {
        mutex.withLock { return post }
    }

    override suspend fun getId(id: Long): Post? {
        mutex.withLock { return post.find { it.id == id } }
    }

    @KtorExperimentalAPI
    override suspend fun updateById(id: Long, data: Post): Post {
        mutex.withLock {
            if (id < 1) throw NotFoundException("Пост не найден")
            val updateData = data.copy()
            post[post.indexOfFirst { it.id == id }] = updateData
            return updateData
        }
    }

    override suspend fun add(data: Post): Post {
        mutex.withLock {
            data.id = getLastId() + 1
            val newData = data.copy()
            if (post.add(newData)) {
                return newData
            } else {
                throw Throwable("Данные не сохранены")
            }
        }
    }

    override suspend fun deleteById(id: Long) {
       mutex.withLock {
           post.removeIf {it.id == id}
       }
    }

    private fun getLastId(): Long {
        return post.maxBy { it.id }?.id ?: 0L
    }
}