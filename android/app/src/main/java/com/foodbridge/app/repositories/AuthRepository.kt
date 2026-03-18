package com.foodbridge.app.repositories

import com.foodbridge.app.models.User
import com.foodbridge.app.services.AuthService
import com.foodbridge.app.services.FirestoreService
import com.google.firebase.auth.FirebaseUser

/**
 * Repository for Authentication operations
 * Combines AuthService and FirestoreService
 */
class AuthRepository(
    private val authService: AuthService = AuthService(),
    private val firestoreService: FirestoreService = FirestoreService()
) {

    /**
     * Register new user
     */
    suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        userType: String // "restaurant" or "ngo"
    ): Result<String> {
        return try {
            // Register with Firebase Auth
            val authResult = authService.registerUser(email, password)
            if (authResult.isFailure) {
                return Result.failure(authResult.exceptionOrNull() ?: Exception("Registration failed"))
            }

            val firebaseUser = authResult.getOrNull()
            val uid = firebaseUser?.uid ?: return Result.failure(Exception("No UID returned"))

            // Create user profile in Firestore
            val user = User(
                uid = uid,
                email = email,
                name = name,
                userType = userType,
                registrationDate = com.google.firebase.Timestamp.now()
            )

            val createResult = firestoreService.createOrUpdateUser(user)
            if (createResult.isFailure) {
                // Delete auth user if Firestore creation fails
                authService.deleteUserAccount()
                return Result.failure(createResult.exceptionOrNull() ?: Exception("Profile creation failed"))
            }

            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Login user
     */
    suspend fun loginUser(email: String, password: String): Result<String> {
        return try {
            val result = authService.loginUser(email, password)
            if (result.isFailure) {
                return Result.failure(result.exceptionOrNull() ?: Exception("Login failed"))
            }

            val uid = result.getOrNull()?.uid ?: return Result.failure(Exception("No UID returned"))
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sign out user
     */
    fun signOutUser() {
        authService.signOut()
    }

    /**
     * Get current user
     */
    fun getCurrentUser(): FirebaseUser? {
        return authService.getCurrentUser()
    }

    /**
     * Check if user is logged in
     */
    fun isUserLoggedIn(): Boolean {
        return authService.isUserLoggedIn()
    }

    /**
     * Send password reset email
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return authService.sendPasswordResetEmail(email)
    }

    /**
     * Update user profile
     */
    suspend fun updateUserProfile(
        uid: String,
        updates: Map<String, Any>
    ): Result<Unit> {
        return try {
            firestoreService.createOrUpdateUser(
                User(uid = uid).copy(
                    name = updates["name"] as? String ?: uid,
                    description = updates["description"] as? String ?: "",
                    location = updates["location"] as? String ?: ""
                    // Add other fields as needed
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
