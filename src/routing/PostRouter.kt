package ru.hwru.server.routing

import entity.Post
import io.ktor.application.call
import io.ktor.features.NotFoundException
import io.ktor.features.ParameterConversionException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein
import ru.hwru.server.repository.post.PostRepository


@KtorExperimentalAPI
fun Routing.api() {
    val repository by kodein().instance<PostRepository>()

    route("/api") {
        get {
            val a = repository.getAll().map {
                it
            }
            call.respond(a)
        }

        get("/post/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val response = repository.getById(id) ?: throw NotFoundException()
            call.respond(response)
        }

        post("/add-post") {
            val clientData = call.receive<Post>()
            val response = repository.add(clientData)
            call.respond(response)
        }

        delete("/delete/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            repository.deleteById(id)
            call.respond(HttpStatusCode.NoContent)
        }

        post("/update/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val clientData = call.receive<Post>()
            val response = repository.updateById(id = id, data = clientData)
            call.respond(response)
        }

        post("/forward/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val me = "Current Author"
            val response = repository.forwardPost(id = id, author = me);
            if (response != null) {
                call.respond(response)
            }
        }

        post("/like/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val response = repository.like(id)
             call.respond(response)
        }

        post("/dislike/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val response = repository.dislike(id)
            call.respond(response)
        }

        post("/share/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val response = repository.share(id)
            call.respond(response)
        }

        post("/share/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val response = repository.share(id)
            call.respond(response)
        }


    }
}


