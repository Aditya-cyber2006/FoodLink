package com.foodbridge.app

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseDataRepository {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun isConfigured(context: Context): Boolean {
        return FirebaseApp.getApps(context).isNotEmpty()
    }

    suspend fun loginAndGetProfile(email: String, password: String): DummyAccount {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid.orEmpty()

        val doc = firestore.collection("users").document(uid).get().await()
        val userType = doc.getString("userType") ?: inferRoleFromEmail(email)
        val name = doc.getString("name") ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }

        if (!doc.exists()) {
            val profile = mapOf(
                "email" to email,
                "name" to name,
                "userType" to userType,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection("users").document(uid).set(profile).await()
        }

        return DummyAccount(
            email = email,
            password = "",
            userType = userType,
            name = name,
            id = uid
        )
    }

    suspend fun addListing(listing: DummyListing) {
        val data = mapOf(
            "id" to listing.id,
            "restaurantName" to listing.restaurantName,
            "foodName" to listing.foodName,
            "quantity" to listing.quantity,
            "expiryTime" to listing.expiryTime,
            "location" to listing.location,
            "status" to listing.status,
            "createdAt" to System.currentTimeMillis()
        )
        firestore.collection("listings").document(listing.id).set(data).await()
    }

    suspend fun getAvailableListings(): List<DummyListing> {
        val snapshot = firestore.collection("listings")
            .whereEqualTo("status", "available")
            .get()
            .await()

        return snapshot.documents.map { doc ->
            DummyListing(
                id = doc.getString("id") ?: doc.id,
                restaurantName = doc.getString("restaurantName") ?: "Restaurant",
                foodName = doc.getString("foodName") ?: "Food",
                quantity = doc.getString("quantity") ?: "0",
                expiryTime = doc.getString("expiryTime") ?: "N/A",
                location = doc.getString("location") ?: "Unknown",
                status = doc.getString("status") ?: "available"
            )
        }
    }

    suspend fun getAllUserProfiles(): List<DummyAccount> {
        val snapshot = firestore.collection("users").get().await()
        return snapshot.documents.map { doc ->
            val email = doc.getString("email") ?: "unknown@foodbridge.app"
            DummyAccount(
                email = email,
                password = "",
                userType = doc.getString("userType") ?: inferRoleFromEmail(email),
                name = doc.getString("name") ?: email.substringBefore("@").replaceFirstChar { it.uppercase() },
                id = doc.id
            )
        }
    }

    private fun inferRoleFromEmail(email: String): String {
        val normalized = email.lowercase()
        return when {
            normalized.contains("admin") -> "admin"
            normalized.contains("ngo") -> "ngo"
            else -> "restaurant"
        }
    }
}