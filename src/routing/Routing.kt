package ru.hwru.server.routing

import entity.Post
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.features.ParameterConversionException
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.routing.Routing
import io.ktor.util.KtorExperimentalAPI
import ru.hwru.server.dto.user.AuthenticationRequestDto
import ru.hwru.server.dto.user.RegistrationRequestDto
import ru.hwru.server.dto.user.UserResponseDto
import ru.hwru.server.entity.user.UserModel
import ru.hwru.server.service.FileService
import ru.hwru.server.service.PostService
import ru.hwru.server.service.UserService

@KtorExperimentalAPI
class Routing constructor(
    private val uploadPath: String,
    private val userService: UserService,
    private val fileService: FileService,
    private val postService: PostService
) {

    fun setup(routing: Routing) {
        with(routing) {
            route("/api/v1") {
                route("/") {
                    post("/registration") {
                        val input = call.receive<RegistrationRequestDto>()
                        val response = userService.register(input)
                        call.respond(response)
                    }

                    post("/authentication") {
                        val input = call.receive<AuthenticationRequestDto>()
                        val response = userService.authenticate(input)
                        call.respond(response)
                    }

                    authenticate("jwt") {
                        route("/me") {
                            get {
                                val me = call.authentication.principal<UserModel>()
                                call.respond(UserResponseDto.fromModel(me!!))
                            }
                        }
                        route("/media") {
                            post {
                                val multipart = call.receiveMultipart()
                                val response = fileService.save(multipart)
                                call.respond(response)
                            }
                        }

                        route("/posts") {
                            get {
                                val posts = postService.getAll().map {
                                    it
                                }
                                call.respond(posts)
                            }

                            get("/{id}") {
                                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                                val response = postService.getById(id)
                                call.respond(response)
                            }

                            post("/add") {
                                val clientData = call.receive<Post>()
                                val response = postService.add(clientData)
                                call.respond(response)
                            }

                            delete("/delete/{id}") {
                                val me = call.authentication.principal<UserModel>() ?: throw ParameterConversionException("me","User")
                                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                                if (postService.getById(id).authorName == me.username) {
                                    postService.delete(id)
                                    call.respond(HttpStatusCode.NoContent)
                                } else {
                                    call.respond(HttpStatusCode.Forbidden)
                                }
                            }

                            post("/update/{id}") {
                                val me = call.authentication.principal<UserModel>() ?: throw ParameterConversionException("me","User")
                                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                                if (postService.getById(id).authorName == me.username) {
                                    val clientData = call.receive<Post>()
                                    val response = postService.updateById(id = id, data = clientData)
                                    call.respond(response)
                                } else {
                                    call.respond(HttpStatusCode.Forbidden)
                                }
                            }

                            post("/forward/{id}") {
                                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                                val me = "Current Author"
                                val response = postService.forwardPost(id = id, authorName = me);
                                call.respond(response)
                            }

                            post("/like/{id}") {
                                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                                val response = postService.like(id)
                                call.respond(response)
                            }

                            post("/dislike/{id}") {
                                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                                val response = postService.dislike(id)
                                call.respond(response)
                            }

                            post("/share/{id}") {
                                val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
                                val response = postService.share(id)
                                call.respond(response)
                            }
                        }
                    }

                    static("/static") {
                        files(uploadPath)
                    }
                }
            }
        }
    }
}