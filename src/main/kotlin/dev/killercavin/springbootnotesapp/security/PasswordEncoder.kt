package dev.killercavin.springbootnotesapp.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoder {
    private val bcrypt = BCryptPasswordEncoder()

    fun encodePassword(rawPassword: String): String {
        return bcrypt.encode(rawPassword)
    }

    fun passwordMatch(rawPassword: String, hashedPassword: String): Boolean {
        return bcrypt.matches(rawPassword, hashedPassword)
    }
}