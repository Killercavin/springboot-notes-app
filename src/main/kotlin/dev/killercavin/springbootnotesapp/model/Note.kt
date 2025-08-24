package dev.killercavin.springbootnotesapp.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "notes")
@CompoundIndex(name = "unique_title", def = "{'title': 1}", unique = true)
data class Note(
    @Id
    val id: ObjectId = ObjectId.get(),
    val title: String,
    val content: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)
