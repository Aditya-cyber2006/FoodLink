package com.foodbridge.app.repositories

import com.foodbridge.app.models.User
import com.foodbridge.app.services.FirestoreService
import com.foodbridge.app.services.StorageService
import android.net.Uri

/**
 * Repository for User-related operations
 */
class UserRepository(
    private val firestoreService: FirestoreService = FirestoreService(),
    private val storageService: StorageService = StorageService()
) {

    /**
     * Get user profile
     */
    suspend fun getUserProfile(uid: String): Result<User?> {
        return firestoreService.getUserById(uid)
    }

    /**
     * Update user profile
     */
    suspend fun updateUserProfile(user: User): Result<Unit> {
        return firestoreService.createOrUpdateUser(user)
    }

    /**
     * Upload user profile picture
     */
    suspend fun uploadProfilePicture(
        userId: String,
        imageUri: Uri
    ): Result<String> {
        return storageService.uploadProfileImage(userId, imageUri)
    }

    /**
     * Update user location
     */
    suspend fun updateUserLocation(
        userId: String,
        latitude: Double,
        longitude: Double
    ): Result<Unit> {
        return try {
            // Get current user
            val userResult = firestoreService.getUserById(userId)
            if (userResult.isFailure) {
                return Result.failure(userResult.exceptionOrNull() ?: Exception("User not found"))
            }

            val user = userResult.getOrNull() ?: return Result.failure(Exception("User not found"))

            // Update location
            val updatedUser = user.copy(
                latitude = latitude,
                longitude = longitude
            )

            firestoreService.createOrUpdateUser(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete user profile
     */
    suspend fun deleteUserProfile(userId: String): Result<Unit> {
        // Delete profile images first
        storageService.deleteProfileImages(userId)

        // Delete user from Firestore
        return firestoreService.deleteUser(userId)
    }

    /**
     * Get user average rating
     */
    suspend fun getUserAverageRating(userId: String): Result<Double> {
        return try {
            val userResult = firestoreService.getUserById(userId)
            if (userResult.isFailure) {
                return Result.failure(userResult.exceptionOrNull() ?: Exception("User not found"))
            }

            val user = userResult.getOrNull() ?: return Result.failure(Exception("User not found"))
            Result.success(user.rating)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update user verification status
     */
    suspend fun updateVerificationStatus(
        userId: String,
        isVerified: Boolean,
        documentUrl: String = ""
    ): Result<Unit> {
        return try {
            val userResult = firestoreService.getUserById(userId)
            if (userResult.isFailure) {
                return Result.failure(userResult.exceptionOrNull() ?: Exception("User not found"))
            }

            val user = userResult.getOrNull() ?: return Result.failure(Exception("User not found"))

            val updatedUser = user.copy(
                isVerified = isVerified,
                documentUrl = documentUrl
            )

            firestoreService.createOrUpdateUser(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
