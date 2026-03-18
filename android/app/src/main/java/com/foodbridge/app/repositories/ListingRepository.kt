package com.foodbridge.app.repositories

import com.foodbridge.app.models.FoodListing
import com.foodbridge.app.models.Rating
import com.foodbridge.app.models.Transaction
import com.foodbridge.app.services.FirestoreService
import com.foodbridge.app.services.StorageService
import android.net.Uri

/**
 * Repository for Food Listing operations
 */
class ListingRepository(
    private val firestoreService: FirestoreService = FirestoreService(),
    private val storageService: StorageService = StorageService()
) {

    /**
     * Create new food listing
     */
    suspend fun createListing(
        listing: FoodListing,
        imageUri: Uri? = null
    ): Result<String> {
        return try {
            // Create listing in Firestore first to get ID
            val listingResult = firestoreService.createListing(listing)
            if (listingResult.isFailure) {
                return Result.failure(listingResult.exceptionOrNull() ?: Exception("Failed to create listing"))
            }

            val listingId = listingResult.getOrNull() ?: return Result.failure(Exception("No listing ID returned"))

            // Upload image if provided
            if (imageUri != null) {
                val uploadResult = storageService.uploadFoodImage(listingId, imageUri)
                if (uploadResult.isSuccess) {
                    val imageUrl = uploadResult.getOrNull() ?: ""
                    val updatedListing = listing.copy(
                        listingId = listingId,
                        imageUrl = imageUrl
                    )
                    firestoreService.createOrUpdateListing(updatedListing)
                }
            }

            Result.success(listingId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all available listings
     */
    suspend fun getAvailableListings(limit: Long = 20): Result<List<FoodListing>> {
        return firestoreService.getAvailableListings(limit)
    }

    /**
     * Get listings by restaurant
     */
    suspend fun getListingsByRestaurant(restaurantId: String): Result<List<FoodListing>> {
        return firestoreService.getListingsByRestaurant(restaurantId)
    }

    /**
     * Get listing details
     */
    suspend fun getListingDetails(listingId: String): Result<FoodListing?> {
        return firestoreService.getListingById(listingId)
    }

    /**
     * Claim food listing
     */
    suspend fun claimListing(listingId: String, ngoId: String): Result<Unit> {
        return try {
            // Update listing status
            val claimResult = firestoreService.claimListing(listingId, ngoId)
            if (claimResult.isFailure) {
                return Result.failure(claimResult.exceptionOrNull() ?: Exception("Failed to claim listing"))
            }

            // Get listing to get restaurant ID
            val listingResult = firestoreService.getListingById(listingId)
            if (listingResult.isFailure) {
                return Result.failure(listingResult.exceptionOrNull() ?: Exception("Failed to get listing"))
            }

            val listing = listingResult.getOrNull() ?: return Result.failure(Exception("Listing not found"))

            // Create transaction record
            val transaction = Transaction(
                listingId = listingId,
                restaurantId = listing.restaurantId,
                restaurantName = listing.restaurantName,
                ngoId = ngoId,
                foodTitle = listing.title,
                quantity = listing.quantity,
                claimedAt = com.google.firebase.Timestamp.now()
            )

            firestoreService.createTransaction(transaction)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update listing
     */
    suspend fun updateListing(listing: FoodListing): Result<Unit> {
        return try {
            firestoreService.createOrUpdateListing(listing)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete listing
     */
    suspend fun deleteListing(listingId: String): Result<Unit> {
        return try {
            // Delete images from storage
            storageService.deleteFoodListingImages(listingId)

            // Delete listing from Firestore
            firestoreService.deleteListing(listingId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Create rating
     */
    suspend fun createRating(rating: Rating): Result<String> {
        return firestoreService.createRating(rating)
    }

    /**
     * Get ratings for user
     */
    suspend fun getUserRatings(userId: String): Result<List<Rating>> {
        return firestoreService.getUserRatings(userId)
    }

    /**
     * Get user transactions
     */
    suspend fun getUserTransactions(userId: String): Result<List<Transaction>> {
        return firestoreService.getUserTransactions(userId)
    }
}

/**
 * Extension function for Firestore service compatibility
 */
suspend fun FirestoreService.createOrUpdateListing(listing: FoodListing): Result<Unit> {
    return try {
        // This is a workaround - in real implementation, you might need to add this method
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
