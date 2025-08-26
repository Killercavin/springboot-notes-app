package dev.killercavin.springbootnotesapp.model.request

import jakarta.validation.constraints.NotBlank

data class UserLoginRequest(
    @field:NotBlank(message = "Email cannot be blank") val email: String,
    @field:NotBlank(message = "Password cannot be blank") val password: String
    )