package com.foodbridge.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.foodbridge.app.R

/**
 * Home Fragment - Main screen with onboarding
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRoleMenu = view.findViewById<ImageButton>(R.id.btn_role_menu)
        val btnOpenRoleMenu = view.findViewById<Button>(R.id.btn_open_role_menu)

        btnRoleMenu.setOnClickListener {
            showRoleMenu(it)
        }

        btnOpenRoleMenu.setOnClickListener {
            showRoleMenu(it)
        }
    }

    private fun showRoleMenu(anchor: View) {
        val popupMenu = PopupMenu(requireContext(), anchor)
        popupMenu.menuInflater.inflate(R.menu.home_role_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            onRoleSelected(menuItem)
        }
        popupMenu.show()
    }

    private fun onRoleSelected(menuItem: MenuItem): Boolean {
        val selectedRole = when (menuItem.itemId) {
            R.id.menu_role_restaurant -> "restaurant"
            R.id.menu_role_ngo -> "ngo"
            R.id.menu_role_admin -> "admin"
            else -> "all"
        }

        val args = bundleOf("selectedRole" to selectedRole)
        findNavController().navigate(R.id.action_home_to_login, args)
        return true
    }
}

