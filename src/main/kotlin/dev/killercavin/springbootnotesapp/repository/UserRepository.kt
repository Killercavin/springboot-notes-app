package dev.killercavin.springbootnotesapp.repository

import dev.killercavin.springbootnotesapp.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, ObjectId> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?
}