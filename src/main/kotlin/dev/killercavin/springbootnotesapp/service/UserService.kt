package dev.killercavin.springbootnotesapp.service

import dev.killercavin.springbootnotesapp.exception.DuplicateUniqueFieldException
import dev.killercavin.springbootnotesapp.exception.ResourceNotFoundException
import dev.killercavin.springbootnotesapp.model.User
import dev.killercavin.springbootnotesapp.model.request.CreateUserRequest
import dev.killercavin.springbootnotesapp.model.request.UserLoginRequest
import dev.killercavin.springbootnotesapp.model.response.UserResponse
import dev.killercavin.springbootnotesapp.model.response.toUserResponseDTO
import dev.killercavin.springbootnotesapp.repository.UserRepository
import dev.killercavin.springbootnotesapp.security.JwtService
import dev.killercavin.springbootnotesapp.security.PasswordEncoder
import dev.killercavin.springbootnotesapp.security.auth.AuthService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun createUser(request: CreateUserRequest): UserResponse {
        val newUser = User(
            name = request.name,
            email = request.email,
            hashedPassword = passwordEncoder.encodePassword(request.password)
        )

        if (userRepository.existsByEmail(request.email)) throw DuplicateUniqueFieldException("User with ${request.email} already exists")

        return userRepository.save(newUser).toUserResponseDTO()
    }

    fun userLogin(request: UserLoginRequest): AuthService.TokenPair {
        val user = userRepository.findByEmail(request.email) ?: throw ResourceNotFoundException("User not found")

        if (!passwordEncoder.passwordMatch(rawPassword = request.password, hashedPassword = user.hashedPassword)) throw BadCredentialsException("Invalid email or password")

        val newAccessToken = jwtService.generateAccessToken(user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        return AuthService.TokenPair(accessToken = newAccessToken, refreshToken = newRefreshToken)
    }
}