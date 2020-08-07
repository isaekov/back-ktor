package ru.hwru.server

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import entity.Post
import io.ktor.application.*
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.runBlocking
import org.kodein.di.generic.*
import org.kodein.di.ktor.KodeinFeature
import org.kodein.di.ktor.kodein
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.hwru.server.dto.user.UserRepositoryInMemoryWithMutexImpl
import ru.hwru.server.repository.post.PostRepository
import ru.hwru.server.repository.post.PostRepositoryImplementation
import ru.hwru.server.repository.user.UserRepository
import ru.hwru.server.repository.user.UserRepositoryImplementation
import ru.hwru.server.routing.Routing
import ru.hwru.server.service.FileService
import ru.hwru.server.service.JWTTokenService
import ru.hwru.server.service.PostService
import ru.hwru.server.service.UserService
import javax.naming.ConfigurationException

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = true) {

    println(
        "Папка аплод: " + (environment.config.propertyOrNull("crud.upload.dir")?.getString() ?: "нету, null")
    )

    install(PartialContent) {
        // Maximum number of ranges that will be accepted from a HTTP request.
        // If the HTTP request specifies more ranges, they will all be merged into a single range.
        maxRangeCount = 10
    }

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

    install(KodeinFeature) {
        constant(tag = "upload-dir") with (environment.config.propertyOrNull("crud.upload.dir")?.getString()
            ?: throw ConfigurationException("Upload dir is not specified"))
        bind<PasswordEncoder>() with eagerSingleton { BCryptPasswordEncoder() }
        bind<JWTTokenService>() with eagerSingleton { JWTTokenService() }
        bind<PostRepositoryImplementation>() with eagerSingleton { PostRepositoryImplementation() }
        bind<PostService>() with singleton { PostService(instance()) }
        bind<FileService>() with eagerSingleton { FileService(instance(tag = "upload-dir")) }
        bind<UserRepository>() with eagerSingleton { UserRepositoryInMemoryWithMutexImpl() }
        bind<UserService>() with eagerSingleton {
            UserService(instance(), instance(), instance()).apply {
                runBlocking {
                    this@apply.save("ildar", "123")
                    this@apply.save("man", "123")
                }
            }
        }

        bind<Routing>() with eagerSingleton {
            Routing(
                instance(tag = "upload-dir"),
                instance(),
                instance(),
                instance()
            )
        }

    install(Authentication) {
        jwt("jwt") {
            val jwtService = JWTTokenService();
            verifier(jwtService.verifier)
            val userService by kodein().instance<UserService>()
            validate {
                val id = it.payload.getClaim("id").asLong()
                userService.getModelById(id)
            }
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


    }

    install(io.ktor.routing.Routing) {
        val routing by kodein().instance<Routing>()
        routing.setup(this)
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

