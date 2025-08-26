package dev.killercavin.springbootnotesapp.model.request

import jakarta.validation.constraints.NotBlank

data class CreateUserRequest(
    @field:NotBlank(message = "Name cannot be blank") val name: String,
    @field:NotBlank(message = "Email cannot be blank") val email: String,
    @field:NotBlank(message = "Password cannot be blank") val password: String
)
