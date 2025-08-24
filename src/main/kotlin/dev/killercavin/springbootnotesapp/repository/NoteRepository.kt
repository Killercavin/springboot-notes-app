package dev.killercavin.springbootnotesapp.repository

import dev.killercavin.springbootnotesapp.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository: MongoRepository<Note, ObjectId> {
    fun existsByTitle(title: String): Boolean
}