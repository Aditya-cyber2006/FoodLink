package com.foodbridge.app.models

import com.google.firebase.Timestamp
import java.io.Serializable

/**
 * User model for FoodBridge
 * Represents both Restaurant and NGO users
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val userType: String = "", // "restaurant" or "ngo"
    val profileImageUrl: String = "",
    val phoneNumber: String = "",
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val registrationDate: Timestamp? = null,
    val isVerified: Boolean = false,
    val rating: Double = 0.0,
    val totalRatings: Int = 0,
    val description: String = "", // For restaurants/NGOs
    val documentUrl: String = "" // For NGO verification
) : Serializable

/**
 * Food Listing model
 * Represents a food item available from a restaurant
 */
data class FoodListing(
    val listingId: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    val title: String = "",
    val description: String = "",
    val foodType: String = "", // "vegetarian", "non-vegetarian", "vegan"
    val quantity: Int = 0,
    val quantityUnit: String = "", // "pieces", "kg", "liters"
    val imageUrl: String = "",
    val expiryTime: Timestamp? = null,
    val createdAt: Timestamp? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: String = "available", // "available", "claimed", "expired"
    val claimedBy: String = "", // NGO UID
    val claimedAt: Timestamp? = null,
    val tags: List<String> = emptyList(),
    val availability: String = "now" // "now", "scheduled"
) : Serializable

/**
 * Notification model
 * Represents in-app notifications
 */
data class Notification(
    val notificationId: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val notificationType: String = "", // "listing_available", "listing_claimed", "message", "notification"
    val relatedListingId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val createdAt: Timestamp? = null,
    val isRead: Boolean = false,
    val actionUrl: String = "",
    val imageUrl: String = ""
) : Serializable

/**
 * Rating model
 * Represents user ratings and reviews
 */
data class Rating(
    val ratingId: String = "",
    val ratedUserId: String = "", // User being rated (restaurant or NGO)
    val ratedByUserId: String = "", // User giving rating
    val ratedByName: String = "",
    val rating: Float = 0f, // 1 to 5 stars
    val review: String = "",
    val category: String = "", // "food_quality", "hygiene", "timeliness" etc
    val createdAt: Timestamp? = null,
    val helpful: Int = 0
) : Serializable

/**
 * Message model
 * For direct messaging between users
 */
data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val receiverId: String = "",
    val messageText: String = "",
    val imageUrl: String = "",
    val timestamp: Timestamp? = null,
    val isRead: Boolean = false,
    val conversationId: String = ""
) : Serializable

/**
 * Conversation model
 * Represents a conversation thread
 */
data class Conversation(
    val conversationId: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    val ngoId: String = "",
    val ngoName: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Timestamp? = null,
    val unreadCount: Int = 0
) : Serializable

/**
 * Transaction/Activity model
 * Logs for food claim transactions
 */
data class Transaction(
    val transactionId: String = "",
    val listingId: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    val ngoId: String = "",
    val ngoName: String = "",
    val foodTitle: String = "",
    val quantity: Int = 0,
    val claimedAt: Timestamp? = null,
    val status: String = "claimed", // "claimed", "completed", "cancelled"
    val notes: String = ""
) : Serializable
