package ru.hwru.server.service

import io.ktor.features.BadRequestException
import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import org.springframework.security.crypto.password.PasswordEncoder
import ru.hwru.server.dto.user.*
import ru.hwru.server.entity.user.UserModel
import ru.hwru.server.exception.InvalidPasswordException
import ru.hwru.server.exception.PasswordChangeException
import ru.hwru.server.repository.user.UserRepository


class UserService(
    private val repo: UserRepository,
    private val tokenService: JWTTokenService,
    private val passwordEncoder: PasswordEncoder
) {
    suspend fun getModelById(id: Long): UserModel? {
        return repo.getById(id)
    }

    suspend fun getByUserName(username: String): UserModel? {
        return repo.getByUsername(username)
    }

    @KtorExperimentalAPI
    suspend fun getById(id: Long): UserResponseDto {
        val model = repo.getById(id) ?: throw NotFoundException()
        return UserResponseDto.fromModel(model)
    }

    @KtorExperimentalAPI
    suspend fun changePassword(id: Long, input: PasswordChangeRequestDto) {
        // TODO: handle concurrency
        val model = repo.getById(id) ?: throw NotFoundException()
        if (!passwordEncoder.matches(input.old, model.password)) {
            throw PasswordChangeException("Wrong password!")
        }
        val copy = model.copy(password = passwordEncoder.encode(input.new))
        repo.save(copy)
    }

    @KtorExperimentalAPI
    suspend fun authenticate(input: AuthenticationRequestDto): AuthenticationResponseDto {
        val model = repo.getByUsername(input.username) ?: throw NotFoundException()
        if (!passwordEncoder.matches(input.password, model.password)) {
            throw InvalidPasswordException("Wrong password!")
        }

        val token = tokenService.generate(model.id)
        return AuthenticationResponseDto(token)
    }

    suspend fun save(username: String, password: String) {
        // TODO: check for existence
        // TODO: handle concurrency
        repo.save(UserModel(username = username, password = passwordEncoder.encode(password)))
        return
    }

    @KtorExperimentalAPI
    suspend fun register(input: RegistrationRequestDto): RegistrationResponseDto {
        if (repo.getByUsername(input.username) == null) {
            repo.save(UserModel(username = input.username, password = passwordEncoder.encode(input.password)))
            val model = repo.getByUsername(input.username)
            val token = tokenService.generate(model!!.id)
            return RegistrationResponseDto(token)
        } else throw BadRequestException("Пользователь с таким логином уже зарегистрирован")
    }
}