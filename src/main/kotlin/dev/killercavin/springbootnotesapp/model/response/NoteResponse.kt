package dev.killercavin.springbootnotesapp.model.response

import dev.killercavin.springbootnotesapp.model.Note
import org.bson.types.ObjectId
import java.time.Instant

data class NoteResponse(
    val id: ObjectId,
    val title: String,
    val content: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

fun Note.toNoteResponseDTO(): NoteResponse = NoteResponse(
    id = this.id,
    title = this.title,
    content = this.content,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)
