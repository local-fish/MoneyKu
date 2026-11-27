package com.example.moneyku.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthUser(
    val email: String,
    val name: String
)

class AuthRepository {
    private val _currentUser = MutableStateFlow<AuthUser?>(null)
    val currentUser: StateFlow<AuthUser?> = _currentUser.asStateFlow()

    suspend fun login(usernameOrEmail: String, passwordHash: String): Result<AuthUser> {
        if (usernameOrEmail.isNotBlank() && passwordHash.isNotBlank()) {
            val user = AuthUser(
                email = usernameOrEmail,
                name = usernameOrEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
            )
            _currentUser.value = user
            return Result.success(user)
        }
        return Result.failure(IllegalArgumentException("Email atau Password tidak boleh kosong"))
    }

    suspend fun register(fullName: String, email: String, passwordHash: String): Result<AuthUser> {
        if (fullName.isNotBlank() && email.isNotBlank() && passwordHash.isNotBlank()) {
            val user = AuthUser(email = email, name = fullName)
            _currentUser.value = user
            return Result.success(user)
        }
        return Result.failure(IllegalArgumentException("Mohon lengkapi formulir pendaftaran"))
    }

    fun logout() {
        _currentUser.value = null
    }
}
