package ru.hwru.server

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import entity.Post
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.NotFoundException
import io.ktor.features.ParameterConversionException
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

import io.ktor.routing.Routing
import io.ktor.server.cio.EngineMain
import io.ktor.util.KtorExperimentalAPI
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.ktor.KodeinFeature
import ru.hwru.server.repository.post.PostRepository
import ru.hwru.server.repository.post.PostRepositoryImplementation
import ru.hwru.server.routing.api

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
                .addDeserializationExclusionStrategy(object : ExclusionStrategy {
                    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                        return false
                    }

                    override fun shouldSkipField(f: FieldAttributes?): Boolean {
                        return if (f != null) {
                            (f.declaringClass == Post::class.java && f.name == "id")
                        } else false
                    }
                })
        }
    }

    install(StatusPages) {
        exception<NotImplementedError> { e ->
            call.respond(HttpStatusCode.NotImplemented)
            throw e
        }
        exception<ParameterConversionException> { e ->
            call.respond(HttpStatusCode.BadRequest)
            throw e
        }
        exception<Throwable> { e ->
            call.respond(HttpStatusCode.InternalServerError)
            throw e
        }
        exception<NotFoundException> { e ->
            call.respond(HttpStatusCode.NotFound)
            throw e
        }
    }
    install(KodeinFeature) {
        bind<PostRepository>() with singleton { PostRepositoryImplementation() }
    }

    install(Routing) {
        api();
    }
}

