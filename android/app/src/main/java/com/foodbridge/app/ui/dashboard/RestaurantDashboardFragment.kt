package com.foodbridge.app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.foodbridge.app.DummyDataRepository
import com.foodbridge.app.DummyListing
import com.foodbridge.app.R

class RestaurantDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodNameInput = view.findViewById<EditText>(R.id.food_name_input)
        val quantityInput = view.findViewById<EditText>(R.id.quantity_input)
        val expiryInput = view.findViewById<EditText>(R.id.expiry_time_input)
        val locationInput = view.findViewById<EditText>(R.id.location_input)
        val addButton = view.findViewById<Button>(R.id.add_listing_button)
        val listingsContainer = view.findViewById<LinearLayout>(R.id.restaurant_listings_container)

        addButton.setOnClickListener {
            val foodName = foodNameInput.text.toString()
            val quantity = quantityInput.text.toString()
            val expiry = expiryInput.text.toString()
            val location = locationInput.text.toString()

            if (foodName.isNotEmpty() && quantity.isNotEmpty() && expiry.isNotEmpty() && location.isNotEmpty()) {
                val newListing = DummyListing(
                    id = "list_${System.currentTimeMillis()}",
                    restaurantName = "Demo Restaurant",
                    foodName = foodName,
                    quantity = quantity,
                    expiryTime = expiry,
                    location = location,
                    status = "available"
                )
                
                DummyDataRepository.addListing(newListing)
                Toast.makeText(context, "Listing added successfully!", Toast.LENGTH_SHORT).show()
                
                // Clear inputs
                foodNameInput.text.clear()
                quantityInput.text.clear()
                expiryInput.text.clear()
                locationInput.text.clear()
                
                // Refresh listings
                displayListings(listingsContainer)
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        displayListings(listingsContainer)
    }

    private fun displayListings(container: LinearLayout) {
        container.removeAllViews()

        val listings = DummyDataRepository.getAvailableListings()
        
        for (listing in listings) {
            val itemView = LinearLayout(context)
            itemView.orientation = LinearLayout.VERTICAL
            itemView.setPadding(16, 16, 16, 16)
            itemView.setBackgroundColor(resources.getColor(R.color.white))
            
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 8, 0, 8)
            itemView.layoutParams = layoutParams

            val foodText = TextView(context)
            foodText.text = listing.foodName
            foodText.textSize = 18f
            foodText.setTextColor(resources.getColor(R.color.primary))
            foodText.setTypeface(null, android.graphics.Typeface.BOLD)

            val quantityText = TextView(context)
            quantityText.text = "Quantity: ${listing.quantity}"
            quantityText.textSize = 14f
            quantityText.setTextColor(resources.getColor(R.color.text_secondary))

            val expiryText = TextView(context)
            expiryText.text = "Expires in: ${listing.expiryTime}"
            expiryText.textSize = 14f
            expiryText.setTextColor(resources.getColor(R.color.warning))

            itemView.addView(foodText)
            itemView.addView(quantityText)
            itemView.addView(expiryText)
            
            container.addView(itemView)
        }
    }
}

