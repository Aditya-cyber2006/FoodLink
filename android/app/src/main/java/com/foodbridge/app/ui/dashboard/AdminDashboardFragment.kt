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

class AdminDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val accounts = loadAccounts()
            val listings = loadListings()

            displayAdminStats(view, accounts, listings)
            displayAllAccounts(view, accounts)
            displayAllListings(view, listings)
        }
    }

    private fun displayAdminStats(view: View, accounts: List<com.foodbridge.app.DummyAccount>, listings: List<DummyListing>) {
        val statsContainer = view.findViewById<LinearLayout>(R.id.admin_stats_container)
        statsContainer.removeAllViews()

        val statsText = TextView(view.context)
        statsText.text = "ADMIN DASHBOARD STATS\n\n" +
                "Total Registered Accounts: ${accounts.size}\n" +
                "Restaurants: ${accounts.count { it.userType == "restaurant" }}\n" +
                "NGOs: ${accounts.count { it.userType == "ngo" }}\n" +
                "Admins: ${accounts.count { it.userType == "admin" }}\n\n" +
                "Total Food Listings: ${listings.size}\n" +
                "Available: ${listings.count { it.status == "available" }}"

        statsText.textSize = 14f
        statsText.setTextColor(resources.getColor(R.color.text_primary))
        statsText.setPadding(16, 16, 16, 16)
        statsContainer.addView(statsText)
    }

    private fun displayAllAccounts(view: View, accounts: List<com.foodbridge.app.DummyAccount>) {
        val accountsContainer = view.findViewById<LinearLayout>(R.id.admin_accounts_container)
        accountsContainer.removeAllViews()

        val titleText = TextView(view.context)
        titleText.text = "ALL REGISTERED ACCOUNTS"
        titleText.textSize = 16f
        titleText.setTypeface(null, android.graphics.Typeface.BOLD)
        titleText.setTextColor(resources.getColor(R.color.primary))
        titleText.setPadding(16, 16, 16, 8)
        accountsContainer.addView(titleText)

        for (account in accounts) {
            val itemView = LinearLayout(view.context)
            itemView.orientation = LinearLayout.VERTICAL
            itemView.setPadding(16, 12, 16, 12)
            itemView.setBackgroundColor(resources.getColor(R.color.light_gray))

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 4, 8, 4)
            itemView.layoutParams = layoutParams

            val emailText = TextView(view.context)
            emailText.text = account.email
            emailText.textSize = 14f
            emailText.setTextColor(resources.getColor(R.color.text_primary))
            emailText.setTypeface(null, android.graphics.Typeface.BOLD)

            val typeText = TextView(view.context)
            typeText.text = "Type: ${account.userType.uppercase()}"
            typeText.textSize = 12f
            typeText.setTextColor(resources.getColor(R.color.secondary))
            typeText.setPadding(0, 4, 0, 0)

            itemView.addView(emailText)
            itemView.addView(typeText)
            accountsContainer.addView(itemView)
        }
    }

    private fun displayAllListings(view: View, listings: List<DummyListing>) {
        val listingsContainer = view.findViewById<LinearLayout>(R.id.admin_listings_container)
        listingsContainer.removeAllViews()

        val titleText = TextView(view.context)
        titleText.text = "ALL FOOD LISTINGS"
        titleText.textSize = 16f
        titleText.setTypeface(null, android.graphics.Typeface.BOLD)
        titleText.setTextColor(resources.getColor(R.color.primary))
        titleText.setPadding(16, 16, 16, 8)
        listingsContainer.addView(titleText)

        for (listing in listings) {
            val itemView = LinearLayout(view.context)
            itemView.orientation = LinearLayout.VERTICAL
            itemView.setPadding(16, 12, 16, 12)
            itemView.setBackgroundColor(resources.getColor(R.color.light_gray))

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 4, 8, 4)
            itemView.layoutParams = layoutParams

            val foodText = TextView(view.context)
            foodText.text = "${listing.foodName} (${listing.restaurantName})"
            foodText.textSize = 14f
            foodText.setTextColor(resources.getColor(R.color.text_primary))
            foodText.setTypeface(null, android.graphics.Typeface.BOLD)

            val detailText = TextView(view.context)
            detailText.text = "Qty: ${listing.quantity} | Location: ${listing.location} | Status: ${listing.status}"
            detailText.textSize = 12f
            detailText.setTextColor(resources.getColor(R.color.text_secondary))
            detailText.setPadding(0, 4, 0, 0)

            itemView.addView(foodText)
            itemView.addView(detailText)
            listingsContainer.addView(itemView)
        }
    }

    private suspend fun loadAccounts(): List<com.foodbridge.app.DummyAccount> {
        val ctx = context ?: return DummyDataRepository.dummyAccounts
        return try {
            if (FirebaseDataRepository.isConfigured(ctx)) {
                FirebaseDataRepository.getAllUserProfiles()
            } else {
                DummyDataRepository.dummyAccounts
            }
        } catch (_: Exception) {
            DummyDataRepository.dummyAccounts
        }
    }

    private suspend fun loadListings(): List<DummyListing> {
        val ctx = context ?: return DummyDataRepository.dummyListings
        return try {
            if (FirebaseDataRepository.isConfigured(ctx)) {
                FirebaseDataRepository.getAvailableListings()
            } else {
                DummyDataRepository.dummyListings
            }
        } catch (_: Exception) {
            DummyDataRepository.dummyListings
        }
    }
}
