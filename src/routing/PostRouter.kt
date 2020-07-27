package ru.hwru.server.routing

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import entity.DataSource
import io.ktor.application.call
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein
import ru.hwru.server.repository.post.PostRepository
import ru.hwru.server.repository.post.PostRepositoryImplementation


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
        }
}


