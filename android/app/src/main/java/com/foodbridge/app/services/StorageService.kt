package com.foodbridge.app.services

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

/**
 * Service for handling Firebase Cloud Storage operations
 */
class StorageService(
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
) {

    companion object {
        private const val FOOD_IMAGES_PATH = "food-images/"
        private const val PROFILE_IMAGES_PATH = "profile-images/"
        private const val MAX_FILE_SIZE = 5 * 1024 * 1024L // 5MB
    }

    /**
     * Upload food image to Cloud Storage
     */
    suspend fun uploadFoodImage(
        listingId: String,
        imageUri: Uri,
        fileName: String = "food_${System.currentTimeMillis()}.jpg"
    ): Result<String> {
        return try {
            val imageRef = firebaseStorage.reference
                .child("$FOOD_IMAGES_PATH$listingId/$fileName")

            imageRef.putFile(imageUri).await()
            val downloadUrl = imageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Upload profile image to Cloud Storage
     */
    suspend fun uploadProfileImage(
        userId: String,
        imageUri: Uri,
        fileName: String = "profile_${System.currentTimeMillis()}.jpg"
    ): Result<String> {
        return try {
            val imageRef = firebaseStorage.reference
                .child("$PROFILE_IMAGES_PATH$userId/$fileName")

            imageRef.putFile(imageUri).await()
            val downloadUrl = imageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete file from Cloud Storage
     */
    suspend fun deleteFile(filePath: String): Result<Unit> {
        return try {
            firebaseStorage.reference.child(filePath).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get download URL for a file
     */
    suspend fun getDownloadUrl(filePath: String): Result<String> {
        return try {
            val downloadUrl = firebaseStorage.reference
                .child(filePath)
                .downloadUrl
                .await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete all images for a listing
     */
    suspend fun deleteFoodListingImages(listingId: String): Result<Unit> {
        return try {
            firebaseStorage.reference
                .child("$FOOD_IMAGES_PATH$listingId")
                .listAll()
                .await()
                .items
                .forEach { it.delete().await() }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete all profile images for a user
     */
    suspend fun deleteProfileImages(userId: String): Result<Unit> {
        return try {
            firebaseStorage.reference
                .child("$PROFILE_IMAGES_PATH$userId")
                .listAll()
                .await()
                .items
                .forEach { it.delete().await() }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
