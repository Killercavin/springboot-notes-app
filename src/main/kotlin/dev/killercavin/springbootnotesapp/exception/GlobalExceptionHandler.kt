package dev.killercavin.springbootnotesapp.exception


import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

data class NoteError(
    val status: Int,
    val error: String,
    val message: String?,
    val timestamp: Instant = Instant.now()
)

@RestControllerAdvice
class GlobalExceptionHandler {

    // duplicate note title handler
    @ExceptionHandler(DuplicateNoteTitleException::class)
    fun handleDuplicateTitle(): ResponseEntity<NoteError> {
        val noteError = NoteError(
            status = HttpStatus.CONFLICT.value(),
            error = HttpStatus.CONFLICT.reasonPhrase,
            message = ""
        )

        return ResponseEntity.status(HttpStatus.CONFLICT).body(noteError)
    }

    // handle body validation error
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBodyValidation(e: MethodArgumentNotValidException): ResponseEntity<NoteError> {
        val bodyError = e.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Body validation failed"
        val noteError = NoteError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = bodyError
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noteError)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException): ResponseEntity<NoteError> {
        val message = e.constraintViolations
            .joinToString(", ") { it.message }

        val noteError = NoteError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = message
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noteError)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidJson(): ResponseEntity<NoteError> {
        val noteError = NoteError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = "Invalid request body"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noteError)
    }

    @ExceptionHandler(NoteNotFoundException::class)
    fun handleNoteNotFound(): ResponseEntity<NoteError> {
        val noteError = NoteError(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ""
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noteError)
    }

    // handle all errors
    @ExceptionHandler(Exception::class)
    fun handleAll(): ResponseEntity<NoteError> {
        val noteError = NoteError(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = "Something went wrong, please try again later"
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(noteError)
    }
}

// custom exception classes
class DuplicateNoteTitleException(message: String): RuntimeException(message)

class NoteNotFoundException(message: String): RuntimeException(message)