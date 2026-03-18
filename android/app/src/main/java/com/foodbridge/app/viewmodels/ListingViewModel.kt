package com.foodbridge.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodbridge.app.models.FoodListing
import com.foodbridge.app.repositories.ListingRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for Food Listing operations
 */
class ListingViewModel(
    private val listingRepository: ListingRepository = ListingRepository()
) : ViewModel() {

    private val _listings = MutableLiveData<List<FoodListing>>(emptyList())
    val listings: LiveData<List<FoodListing>> = _listings

    private val _selectedListing = MutableLiveData<FoodListing?>(null)
    val selectedListing: LiveData<FoodListing?> = _selectedListing

    private val _loadingState = MutableLiveData<LoadingState>(LoadingState.Idle)
    val loadingState: LiveData<LoadingState> = _loadingState

    private val _errorMessage = MutableLiveData<String>("")
    val errorMessage: LiveData<String> = _errorMessage

    /**
     * Load available listings
     */
    fun loadAvailableListings() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading

            val result = listingRepository.getAvailableListings()

            if (result.isSuccess) {
                _listings.value = result.getOrNull() ?: emptyList()
                _loadingState.value = LoadingState.Success
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to load listings"
                _loadingState.value = LoadingState.Error(_errorMessage.value!!)
            }
        }
    }

    /**
     * Load restaurant's listings
     */
    fun loadRestaurantListings(restaurantId: String) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading

            val result = listingRepository.getListingsByRestaurant(restaurantId)

            if (result.isSuccess) {
                _listings.value = result.getOrNull() ?: emptyList()
                _loadingState.value = LoadingState.Success
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to load listings"
                _loadingState.value = LoadingState.Error(_errorMessage.value!!)
            }
        }
    }

    /**
     * Select a listing
     */
    fun selectListing(listing: FoodListing) {
        _selectedListing.value = listing
    }

    /**
     * Load listing details
     */
    fun loadListingDetails(listingId: String) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading

            val result = listingRepository.getListingDetails(listingId)

            if (result.isSuccess) {
                _selectedListing.value = result.getOrNull()
                _loadingState.value = LoadingState.Success
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to load listing"
                _loadingState.value = LoadingState.Error(_errorMessage.value!!)
            }
        }
    }

    /**
     * Claim a listing
     */
    fun claimListing(listingId: String, ngoId: String) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading

            val result = listingRepository.claimListing(listingId, ngoId)

            if (result.isSuccess) {
                // Reload listings to reflect status change
                loadAvailableListings()
                _loadingState.value = LoadingState.Success
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to claim listing"
                _loadingState.value = LoadingState.Error(_errorMessage.value!!)
            }
        }
    }

    /**
     * Delete listing
     */
    fun deleteListing(listingId: String) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading

            val result = listingRepository.deleteListing(listingId)

            if (result.isSuccess) {
                // Remove from list
                val updatedList = _listings.value?.filter { it.listingId != listingId } ?: emptyList()
                _listings.value = updatedList
                _loadingState.value = LoadingState.Success
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to delete listing"
                _loadingState.value = LoadingState.Error(_errorMessage.value!!)
            }
        }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _errorMessage.value = ""
    }
}

/**
 * State management for loading
 */
sealed class LoadingState {
    data object Idle : LoadingState()
    data object Loading : LoadingState()
    data object Success : LoadingState()
    data class Error(val message: String) : LoadingState()
}
