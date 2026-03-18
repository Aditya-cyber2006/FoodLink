package com.foodbridge.app.services

import com.foodbridge.app.models.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

/**
 * Service for handling Firestore database operations
 */
class FirestoreService(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val LISTINGS_COLLECTION = "listings"
        private const val NOTIFICATIONS_COLLECTION = "notifications"
        private const val RATINGS_COLLECTION = "ratings"
        private const val MESSAGES_COLLECTION = "messages"
        private const val CONVERSATIONS_COLLECTION = "conversations"
        private const val TRANSACTIONS_COLLECTION = "transactions"
    }

    // ========== USER OPERATIONS ==========

    /**
     * Create or update user profile
     */
    suspend fun createOrUpdateUser(user: User): Result<Unit> {
        return try {
            firestore.collection(USERS_COLLECTION)
                .document(user.uid)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get user by UID
     */
    suspend fun getUserById(uid: String): Result<User?> {
        return try {
            val document = firestore.collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .await()
            Result.success(document.toObject(User::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete user profile
     */
    suspend fun deleteUser(uid: String): Result<Unit> {
        return try {
            firestore.collection(USERS_COLLECTION)
                .document(uid)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== FOOD LISTING OPERATIONS ==========

    /**
     * Create new food listing
     */
    suspend fun createListing(listing: FoodListing): Result<String> {
        return try {
            val documentRef = firestore.collection(LISTINGS_COLLECTION).document()
            val listingWithId = listing.copy(listingId = documentRef.id)
            documentRef.set(listingWithId).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all available listings with pagination
     */
    suspend fun getAvailableListings(limit: Long = 20): Result<List<FoodListing>> {
        return try {
            val listings = firestore.collection(LISTINGS_COLLECTION)
                .whereEqualTo("status", "available")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            Result.success(listings.toObjects(FoodListing::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get listings by restaurant ID
     */
    suspend fun getListingsByRestaurant(restaurantId: String): Result<List<FoodListing>> {
        return try {
            val listings = firestore.collection(LISTINGS_COLLECTION)
                .whereEqualTo("restaurantId", restaurantId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            Result.success(listings.toObjects(FoodListing::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get listing by ID
     */
    suspend fun getListingById(listingId: String): Result<FoodListing?> {
        return try {
            val document = firestore.collection(LISTINGS_COLLECTION)
                .document(listingId)
                .get()
                .await()
            Result.success(document.toObject(FoodListing::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update listing status
     */
    suspend fun updateListingStatus(listingId: String, status: String): Result<Unit> {
        return try {
            firestore.collection(LISTINGS_COLLECTION)
                .document(listingId)
                .update("status", status)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Claim a food listing
     */
    suspend fun claimListing(listingId: String, ngoId: String): Result<Unit> {
        return try {
            firestore.collection(LISTINGS_COLLECTION)
                .document(listingId)
                .update(
                    mapOf(
                        "status" to "claimed",
                        "claimedBy" to ngoId,
                        "claimedAt" to com.google.firebase.Timestamp.now()
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete listing
     */
    suspend fun deleteListing(listingId: String): Result<Unit> {
        return try {
            firestore.collection(LISTINGS_COLLECTION)
                .document(listingId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== NOTIFICATION OPERATIONS ==========

    /**
     * Create notification
     */
    suspend fun createNotification(notification: Notification): Result<String> {
        return try {
            val documentRef = firestore.collection(NOTIFICATIONS_COLLECTION).document()
            val notificationWithId = notification.copy(notificationId = documentRef.id)
            documentRef.set(notificationWithId).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get user notifications
     */
    suspend fun getUserNotifications(userId: String): Result<List<Notification>> {
        return try {
            val notifications = firestore.collection(NOTIFICATIONS_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .await()
            Result.success(notifications.toObjects(Notification::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mark notification as read
     */
    suspend fun markNotificationAsRead(notificationId: String): Result<Unit> {
        return try {
            firestore.collection(NOTIFICATIONS_COLLECTION)
                .document(notificationId)
                .update("isRead", true)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== RATING OPERATIONS ==========

    /**
     * Create rating
     */
    suspend fun createRating(rating: Rating): Result<String> {
        return try {
            val documentRef = firestore.collection(RATINGS_COLLECTION).document()
            val ratingWithId = rating.copy(ratingId = documentRef.id)
            documentRef.set(ratingWithId).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get ratings for a user
     */
    suspend fun getUserRatings(userId: String): Result<List<Rating>> {
        return try {
            val ratings = firestore.collection(RATINGS_COLLECTION)
                .whereEqualTo("ratedUserId", userId)
                .get()
                .await()
            Result.success(ratings.toObjects(Rating::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== TRANSACTION OPERATIONS ==========

    /**
     * Create transaction log
     */
    suspend fun createTransaction(transaction: Transaction): Result<String> {
        return try {
            val documentRef = firestore.collection(TRANSACTIONS_COLLECTION).document()
            val transactionWithId = transaction.copy(transactionId = documentRef.id)
            documentRef.set(transactionWithId).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get user transaction history
     */
    suspend fun getUserTransactions(userId: String): Result<List<Transaction>> {
        return try {
            val transactions = firestore.collection(TRANSACTIONS_COLLECTION)
                .whereEqualTo("ngoId", userId)
                .orderBy("claimedAt", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .await()
            Result.success(transactions.toObjects(Transaction::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
