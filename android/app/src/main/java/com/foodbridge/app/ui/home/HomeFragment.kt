package com.foodbridge.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
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

        // Button Click Listeners
        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val btnRegister = view.findViewById<Button>(R.id.btn_register)
        val btnGetStarted = view.findViewById<Button>(R.id.btn_get_started)

        btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_login)
        }

        btnRegister.setOnClickListener {
            // TODO: Navigate to Register screen
        }

        btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_login)
        }
    }
}

