package repository.post

import io.ktor.application.Application
import io.ktor.config.MapApplicationConfig
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.io.core.Input
import kotlinx.io.streams.asInput
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertTrue

import org.junit.jupiter.api.Assertions.*
import ru.hwru.server.module

internal class PostRepositoryImplementationTest {
    private val jsonContentType = ContentType.Application.Json.withCharset(Charsets.UTF_8)
    private val multipartBoundary = "***blob***"
    private val multipartContentType =
        ContentType.MultiPart.FormData.withParameter("boundary", multipartBoundary).toString()
    private val uploadPath = Files.createTempDirectory("test").toString()
    @KtorExperimentalAPI
    private val configure: Application.() -> Unit = {
        (environment.config as MapApplicationConfig).apply {
            put("ncraft.upload.dir", uploadPath)
        }
        module()
    }


    @Test
    fun like() {
    }

    @Test
    fun dislike() {
    }

    @Test
    fun share() {
    }

    @Test
    fun forwardPost() {
    }

    @Test
    fun getAll() {
        println(uploadPath)
        withTestApplication(configure) {
            handleRequest(HttpMethod.Get, "/api/v1/posts").run {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(jsonContentType, response.contentType())
            }
        }
    }

    @Test
    fun getById() {
    }

    @Test
    fun updateById() {
    }

    @Test
    fun add() {
    }

    @Test
    fun deleteById() {
    }
}