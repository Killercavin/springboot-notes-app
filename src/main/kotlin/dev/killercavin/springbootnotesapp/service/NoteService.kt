package dev.killercavin.springbootnotesapp.service

import dev.killercavin.springbootnotesapp.exception.DuplicateNoteTitleException
import dev.killercavin.springbootnotesapp.exception.InvalidObjectIdException
import dev.killercavin.springbootnotesapp.exception.NoteNotFoundException
import dev.killercavin.springbootnotesapp.model.Note
import dev.killercavin.springbootnotesapp.model.request.CreateNoteRequest
import dev.killercavin.springbootnotesapp.model.response.NoteResponse
import dev.killercavin.springbootnotesapp.model.response.toResponseDTO
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
            throw DuplicateNoteTitleException("Note with title '${request.title}' already exists")
        }

        return noteRepository.save(newNote).toResponseDTO()
    }

    // fetch notes
    fun getNotes(): List<NoteResponse> {
        return noteRepository.findAll().map { it.toResponseDTO() }
    }

    // fetch note by id
    fun noteById(id: String): NoteResponse {
        val objectId = try {
            ObjectId(id)
        } catch (e: IllegalArgumentException) {
            throw InvalidObjectIdException("Invalid ObjectId format: '$id'")
        }

        val note = noteRepository.findByIdOrNull(objectId)
            ?: throw NoteNotFoundException("Note with id '$id' not found")

        return note.toResponseDTO()
    }
}