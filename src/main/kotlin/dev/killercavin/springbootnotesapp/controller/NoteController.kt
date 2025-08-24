package dev.killercavin.springbootnotesapp.controller

import dev.killercavin.springbootnotesapp.model.request.CreateNoteRequest
import dev.killercavin.springbootnotesapp.model.response.NoteResponse
import dev.killercavin.springbootnotesapp.service.NoteService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notes")
class NoteController(private val noteService: NoteService) {

    // PUT notes
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createNote(@Valid @RequestBody request: CreateNoteRequest): NoteResponse {
        return noteService.addNote(request)
    }

    // GET all notes
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getNotes(): List<NoteResponse> {
        return noteService.getNotes()
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun noteById(@Valid @PathVariable id: String): NoteResponse {
        return noteService.noteById(id)
    }
}