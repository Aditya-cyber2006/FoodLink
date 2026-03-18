package com.foodbridge.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodbridge.app.repositories.AuthRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for Authentication operations
 */
class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> = _authState

    private val _userUid = MutableLiveData<String>("")
    val userUid: LiveData<String> = _userUid

    private val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String> = _errorMessage

    /**
     * Register new user
     */
    fun registerUser(
        email: String,
        password: String,
        name: String,
        userType: String
    ) {
        if (!isInputValid(email, password, name)) {
            _errorMessage.value = "Please fill all fields correctly"
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.registerUser(email, password, name, userType)

            if (result.isSuccess) {
                val uid = result.getOrNull() ?: ""
                _userUid.value = uid
                _authState.value = AuthState.Success("Registration successful")
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Registration failed"
                _authState.value = AuthState.Error(_errorMessage.value!!)
            }
        }
    }

    /**
     * Login user
     */
    fun loginUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Email and password cannot be empty"
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.loginUser(email, password)

            if (result.isSuccess) {
                val uid = result.getOrNull() ?: ""
                _userUid.value = uid
                _authState.value = AuthState.Success("Login successful")
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Login failed"
                _authState.value = AuthState.Error(_errorMessage.value!!)
            }
        }
    }

    /**
     * Sign out user
     */
    fun signOutUser() {
        authRepository.signOutUser()
        _userUid.value = ""
        _authState.value = AuthState.Idle
    }

    /**
     * Check if user is already logged in
     */
    fun checkUserStatus() {
        if (authRepository.isUserLoggedIn()) {
            val uid = authRepository.getCurrentUser()?.uid ?: ""
            _userUid.value = uid
            _authState.value = AuthState.Success("Already logged in")
        }
    }

    /**
     * Send password reset email
     */
    fun sendPasswordResetEmail(email: String) {
        if (email.isBlank()) {
            _errorMessage.value = "Email cannot be empty"
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.sendPasswordResetEmail(email)

            if (result.isSuccess) {
                _authState.value = AuthState.Success("Password reset email sent")
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to send reset email"
                _authState.value = AuthState.Error(_errorMessage.value!!)
            }
        }
    }

    /**
     * Validate input
     */
    private fun isInputValid(email: String, password: String, name: String): Boolean {
        return email.isNotBlank() &&
                email.contains("@") &&
                password.length >= 6 &&
                name.isNotBlank()
    }
}

/**
 * State management for authentication
 */
sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
