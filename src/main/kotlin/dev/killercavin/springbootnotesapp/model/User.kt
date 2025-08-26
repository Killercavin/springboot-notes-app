package dev.killercavin.springbootnotesapp.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "users")
@CompoundIndex(name = "unique_email", def = "{'email': 1}", unique = true)
data class User(
    @Id
    val id: ObjectId = ObjectId.get(),
    val name: String,
    val email: String,
    val hashedPassword: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)
