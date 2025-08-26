package dev.killercavin.springbootnotesapp.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("JWT_SECRET") private val jwtSecret: String
) {
    val secretKey: SecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))

    private val accessTokenValidity = 15L * 60L * 1000L // valid for 15 minutes
    val refreshTokenValidity = 7L * 24 * 60L * 1000L // valid for a week

    // token generation
    private fun tokenGeneration(
        userId: String,
        type: String,
        expiry: Long
    ): String {
        val timeIssued = Date()
        val expiryTime = Date(timeIssued.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(timeIssued)
            .expiration(expiryTime)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId: String): String {
        return tokenGeneration(userId = userId, type = "access", expiry = accessTokenValidity)
    }

    fun generateRefreshToken(userId: String): String {
        return tokenGeneration(userId = userId, type = "refresh", expiry = refreshTokenValidity)
    }

    fun parseAllClaims(token: String): Claims? {
        val rawToken = if (token.startsWith("Bearer ")) token.removePrefix("Bearer ") else token
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            null
        }
    }

    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "access"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "refresh"
    }

    fun getUserIdFromToken(token: String): String {
        val claims = parseAllClaims(token) ?: throw IllegalArgumentException("Invalid token")
        return claims.subject
    }
}