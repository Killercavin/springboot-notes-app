package dev.killercavin.springbootnotesapp.exception


import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.Instant

data class AppError(
    val status: Int,
    val error: String,
    val message: String?,
    val timestamp: Instant = Instant.now()
)

@RestControllerAdvice
class GlobalExceptionHandler {

    // duplicate note title handler
    @ExceptionHandler(DuplicateUniqueFieldException::class)
    fun handleDuplicateField(e: DuplicateUniqueFieldException): ResponseEntity<AppError> {
        val appError = AppError(
            status = HttpStatus.CONFLICT.value(),
            error = HttpStatus.CONFLICT.reasonPhrase,
            message = e.message
        )

        return ResponseEntity.status(HttpStatus.CONFLICT).body(appError)
    }

    // handle body validation error
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBodyValidation(e: MethodArgumentNotValidException): ResponseEntity<AppError> {
        val bodyError = e.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Body validation failed"
        val appError = AppError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = bodyError
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(appError)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException): ResponseEntity<AppError> {
        val message = e.constraintViolations
            .joinToString(", ") { it.message }

        val appError = AppError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = message
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(appError)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidJson(): ResponseEntity<AppError> {
        val appError = AppError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = "Invalid request body"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(appError)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(e: ResourceNotFoundException): ResponseEntity<AppError> {
        val appError = AppError(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = e.message
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(appError)
    }

    // handle all errors
    /*@ExceptionHandler(Exception::class)
    fun handleAll(): ResponseEntity<AppError> {
        val appError = AppError(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = "Something went wrong, please try again later"
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(appError)
    }*/

    @ExceptionHandler(MissingPathVariableException::class)
    fun handleMissingPathVar(e: MissingPathVariableException): ResponseEntity<AppError> {
        val appError = AppError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = "Missing path variable: ${e.variableName}"
        )
        return ResponseEntity.badRequest().body(appError)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(e: MethodArgumentTypeMismatchException): ResponseEntity<AppError> {
        val appError = AppError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = "Invalid type for '${e.name}': expected ${e.requiredType?.simpleName}"
        )
        return ResponseEntity.badRequest().body(appError)
    }

    @ExceptionHandler(InvalidObjectIdException::class)
    fun handleInvalidObjectId(e: InvalidObjectIdException): ResponseEntity<AppError> {
        val appError = AppError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = e.message
        )
        return ResponseEntity.badRequest().body(appError)
    }
}

// custom exception classes
class DuplicateUniqueFieldException(message: String): RuntimeException(message)

class ResourceNotFoundException(message: String): RuntimeException(message)

class InvalidObjectIdException(message: String): RuntimeException(message)