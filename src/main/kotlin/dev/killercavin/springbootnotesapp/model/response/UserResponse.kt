package dev.killercavin.springbootnotesapp.model.response

import dev.killercavin.springbootnotesapp.model.User
import org.bson.types.ObjectId
import java.time.Instant

data class UserResponse(
    val id: ObjectId,
    val name: String,
    val email: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

fun User.toUserResponseDTO(): UserResponse {
    return UserResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
