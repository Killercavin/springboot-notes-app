package dev.killercavin.springbootnotesapp.service

import dev.killercavin.springbootnotesapp.exception.DuplicateUniqueFieldException
import dev.killercavin.springbootnotesapp.exception.InvalidObjectIdException
import dev.killercavin.springbootnotesapp.exception.ResourceNotFoundException
import dev.killercavin.springbootnotesapp.model.Note
import dev.killercavin.springbootnotesapp.model.request.CreateNoteRequest
import dev.killercavin.springbootnotesapp.model.response.NoteResponse
import dev.killercavin.springbootnotesapp.model.response.toNoteResponseDTO
import dev.killercavin.springbootnotesapp.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class NoteService(private val noteRepository: NoteRepository) {

    // add a note
    fun addNote(request: CreateNoteRequest): NoteResponse {

        val newNote = Note(
            title = request.title,
            content = request.content
        )

        if (noteRepository.existsByTitle(request.title)) {
            throw DuplicateUniqueFieldException("Note with title '${request.title}' already exists")
        }

        return noteRepository.save(newNote).toNoteResponseDTO()
    }

    // fetch notes
    fun getNotes(): List<NoteResponse> {
        return noteRepository.findAll().map { it.toNoteResponseDTO() }
    }

    // fetch note by id
    fun noteById(id: String): NoteResponse {
        val objectId = try {
            ObjectId(id)
        } catch (e: IllegalArgumentException) {
            throw InvalidObjectIdException("Invalid ObjectId format: '$id'")
        }

        val note = noteRepository.findByIdOrNull(objectId)
            ?: throw ResourceNotFoundException("Note with id '$id' not found")

        return note.toNoteResponseDTO()
    }
}