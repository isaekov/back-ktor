package ru.hwru.server

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authentication
import io.ktor.util.pipeline.PipelineContext
import ru.hwru.server.entity.user.UserModel

val <T: Any> PipelineContext<T, ApplicationCall>.me
    get() = call.authentication.principal<UserModel>()