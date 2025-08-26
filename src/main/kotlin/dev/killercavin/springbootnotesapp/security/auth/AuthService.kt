package dev.killercavin.springbootnotesapp.security.auth

import dev.killercavin.springbootnotesapp.security.JwtService
import dev.killercavin.springbootnotesapp.service.UserService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userService: UserService
) {
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )
}