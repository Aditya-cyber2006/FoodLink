package com.foodbridge.app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.foodbridge.app.DummyDataRepository
import com.foodbridge.app.DummyListing
import com.foodbridge.app.FirebaseDataRepository
import com.foodbridge.app.R
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class NGODashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ngo_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listingsContainer = view.findViewById<LinearLayout>(R.id.ngo_listings_container)
        displayAvailableListings(listingsContainer)
    }

    private fun displayAvailableListings(container: LinearLayout) {
        viewLifecycleOwner.lifecycleScope.launch {
            container.removeAllViews()

            val listings = loadListings()

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

                val restaurantText = TextView(context)
                restaurantText.text = listing.restaurantName
                restaurantText.textSize = 16f
                restaurantText.setTextColor(resources.getColor(R.color.primary))
                restaurantText.setTypeface(null, android.graphics.Typeface.BOLD)

                val foodText = TextView(context)
                foodText.text = listing.foodName
                foodText.textSize = 16f
                foodText.setTextColor(resources.getColor(R.color.text_primary))
                foodText.setTypeface(null, android.graphics.Typeface.BOLD)
                foodText.setPadding(0, 8, 0, 0)

                val quantityText = TextView(context)
                quantityText.text = "Quantity: ${listing.quantity}"
                quantityText.textSize = 13f
                quantityText.setTextColor(resources.getColor(R.color.text_secondary))

                val locationText = TextView(context)
                locationText.text = "Location: ${listing.location}"
                locationText.textSize = 13f
                locationText.setTextColor(resources.getColor(R.color.text_secondary))

                val expiryText = TextView(context)
                expiryText.text = "Pick up in: ${listing.expiryTime}"
                expiryText.textSize = 13f
                expiryText.setTextColor(resources.getColor(R.color.warning))

                val claimButton = Button(context)
                claimButton.text = "Claim This Food"
                claimButton.setBackgroundColor(resources.getColor(R.color.secondary))
                claimButton.setTextColor(resources.getColor(R.color.white))
                claimButton.setPadding(0, 16, 0, 16)

                val buttonParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                buttonParams.setMargins(0, 12, 0, 0)
                claimButton.layoutParams = buttonParams

                claimButton.setOnClickListener {
                    android.widget.Toast.makeText(context, "Food claimed! Pickup arranged.", android.widget.Toast.LENGTH_SHORT).show()
                }

                itemView.addView(restaurantText)
                itemView.addView(foodText)
                itemView.addView(quantityText)
                itemView.addView(locationText)
                itemView.addView(expiryText)
                itemView.addView(claimButton)

                container.addView(itemView)
            }
        }
    }

    private suspend fun loadListings(): List<DummyListing> {
        val ctx = context ?: return DummyDataRepository.getAvailableListings()
        return try {
            if (FirebaseDataRepository.isConfigured(ctx)) {
                FirebaseDataRepository.getAvailableListings()
            } else {
                DummyDataRepository.getAvailableListings()
            }
        } catch (_: Exception) {
            DummyDataRepository.getAvailableListings()
        }
    }
}
