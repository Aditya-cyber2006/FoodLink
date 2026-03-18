package com.foodbridge.app

data class DummyAccount(
    val email: String,
    val password: String,
    val userType: String, // "restaurant", "ngo", "admin"
    val name: String,
    val id: String
)

data class DummyListing(
    val id: String,
    val restaurantName: String,
    val foodName: String,
    val quantity: String,
    val expiryTime: String,
    val location: String,
    val status: String = "available"
)

object DummyDataRepository {
    
    // Dummy login accounts for testing
    val dummyAccounts = listOf(
        DummyAccount(
            email = "restaurant@demo.com",
            password = "demo123",
            userType = "restaurant",
            name = "Demo Restaurant",
            id = "rest_001"
        ),
        DummyAccount(
            email = "ngo@demo.com",
            password = "demo123",
            userType = "ngo",
            name = "Demo NGO",
            id = "ngo_001"
        ),
        DummyAccount(
            email = "admin@demo.com",
            password = "demo123",
            userType = "admin",
            name = "Admin",
            id = "admin_001"
        )
    )

    // Dummy food listings
    val dummyListings = mutableListOf(
        DummyListing(
            id = "list_001",
            restaurantName = "Pizza Palace",
            foodName = "Leftover Pizza Slices",
            quantity = "8 slices",
            expiryTime = "2 hours",
            location = "Downtown",
            status = "available"
        ),
        DummyListing(
            id = "list_002",
            restaurantName = "Burger King",
            foodName = "Unsold Burgers",
            quantity = "12 pieces",
            expiryTime = "1 hour",
            location = "Mall",
            status = "available"
        ),
        DummyListing(
            id = "list_003",
            restaurantName = "Biryani House",
            foodName = "Rice & Curry",
            quantity = "4 portions",
            expiryTime = "30 mins",
            location = "City Center",
            status = "available"
        ),
        DummyListing(
            id = "list_004",
            restaurantName = "Cafe Coffee",
            foodName = "Sandwich Trays",
            quantity = "10 sandwiches",
            expiryTime = "3 hours",
            location = "Downtown",
            status = "available"
        )
    )

    // Add new listing
    fun addListing(listing: DummyListing) {
        dummyListings.add(listing)
    }

    // Get listings by status
    fun getAvailableListings(): List<DummyListing> {
        return dummyListings.filter { it.status == "available" }
    }
}
