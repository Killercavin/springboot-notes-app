package dev.killercavin.springbootnotesapp.model.request

import jakarta.validation.constraints.NotBlank

data class CreateNoteRequest(
    @field:NotBlank(message = "Title cannot be blank")
    val title: String,

    @field:NotBlank(message = "Content cannot be blank")
    val content: String
)
