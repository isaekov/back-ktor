package ru.hwru.server.repository.post

import entity.DataSource
import entity.Post
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
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
    override suspend fun like(id: Long): Post {
        mutex.withLock {
            return when (val postId = post.indexOfFirst { it.id == id }) {
                -1 -> {
                    throw NotFoundException()
                }
                else -> {
                    val currentPost = post[postId]
                    val likeMe = !currentPost.likeMe;
                    val copy = currentPost.copy(likeCount = currentPost.likeCount + 1, likeMe = likeMe)
                    try {
                        post[postId] = copy
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        println("size: ${post.size}")
                        println(postId)
                    }
                    copy
                }
            }
        }
    }
    @KtorExperimentalAPI
    override suspend fun dislike(id: Long): Post {
        mutex.withLock {
            return when (val postId = post.indexOfFirst { it.id == id }) {
                -1 -> {
                    throw NotFoundException()
                }
                else -> {
                    val currentPost = post[postId]
                    val likeMe = !currentPost.likeMe;
                    val copy = currentPost.copy(likeCount = currentPost.likeCount - 1, likeMe = likeMe)
                    try {
                        post[postId] = copy
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        println("size: ${post.size}")
                        println(postId)
                    }
                    copy
                }
            }
        }
    }
    @KtorExperimentalAPI
    override suspend fun share(id: Long): Post {
        mutex.withLock {
            return when (val postId = post.indexOfFirst { it.id == id }) {
                -1 -> {
                    throw NotFoundException()
                }
                else -> {
                    val currentPost = post[postId]
                    val shareMe = !currentPost.shareMe
                    val copy = currentPost.copy(shareCount = currentPost.likeCount - 1, shareMe = shareMe)
                    try {
                        post[postId] = copy
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        println("size: ${post.size}")
                        println(postId)
                    }
                    copy
                }
            }
        }
    }
    @KtorExperimentalAPI
    override suspend fun forwardPost(id: Long, author: String): Post {
        mutex.withLock {
            val source = post.find { it.id == id }
            val newPost = Post(
                id = getLastId() + 1,
                authorName = author,
                createDate = "123412342",
                post = source
            )
            post.add(newPost);
            return post.last()
        }
    }
    @KtorExperimentalAPI
    override suspend fun getAll(): List<Post> {
        mutex.withLock {
            return post.toList()
        }
    }
    @KtorExperimentalAPI
    override suspend fun getById(id: Long): Post? {
        mutex.withLock {
            return post.find { it.id == id }
        }
    }
    @KtorExperimentalAPI
    override suspend fun    updateById(id: Long, data: Post): Post {
        mutex.withLock {
            return when (val index = post.indexOfFirst { it.id == id }) {
                -1 -> {
                    throw NotFoundException("Нет такой записи")
                }
                else -> {
                    post[index] = data
                    data
                }
            }
        }
    }
    @KtorExperimentalAPI
    override suspend fun add(data: Post): Post {
        mutex.withLock {
            val copy = data.copy(id = getLastId() + 1)
            post.add(copy)
            return copy
        }
    }


    @KtorExperimentalAPI
    override suspend fun deleteById(id: Long) {
        mutex.withLock {
            post.removeIf { it.id == id }
        }
    }

    private fun getLastId(): Long {
        return post.maxBy { it.id }?.id ?: 0L
    }
}