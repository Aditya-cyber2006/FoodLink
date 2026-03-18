package com.foodbridge.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.foodbridge.app.DummyDataRepository
import com.foodbridge.app.R

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailInput = view.findViewById<EditText>(R.id.login_email_input)
        val passwordInput = view.findViewById<EditText>(R.id.login_password_input)
        val loginButton = view.findViewById<Button>(R.id.login_button)
        val restaurantDemoButton = view.findViewById<Button>(R.id.login_demo_restaurant_button)
        val ngoDemoButton = view.findViewById<Button>(R.id.login_demo_ngo_button)
        val adminDemoButton = view.findViewById<Button>(R.id.login_demo_admin_button)

        // Pre-fill with restaurant demo account
        emailInput.setText("restaurant@demo.com")
        passwordInput.setText("demo123")

        loginButton.setOnClickListener {
            performLogin(emailInput.text.toString(), passwordInput.text.toString())
        }

        restaurantDemoButton.setOnClickListener {
            emailInput.setText("restaurant@demo.com")
            passwordInput.setText("demo123")
            performLogin("restaurant@demo.com", "demo123")
        }

        ngoDemoButton.setOnClickListener {
            emailInput.setText("ngo@demo.com")
            passwordInput.setText("demo123")
            performLogin("ngo@demo.com", "demo123")
        }

        adminDemoButton.setOnClickListener {
            emailInput.setText("admin@demo.com")
            passwordInput.setText("demo123")
            performLogin("admin@demo.com", "demo123")
        }
    }

    private fun performLogin(email: String, password: String) {
        // Validate with dummy accounts
        val account = DummyDataRepository.dummyAccounts.find {
            it.email == email && it.password == password
        }

        if (account != null) {
            Toast.makeText(context, "Login successful! Welcome", Toast.LENGTH_SHORT).show()
            
            // Navigate based on user type
            when (account.userType) {
                "restaurant" -> findNavController().navigate(R.id.action_login_to_restaurant_dashboard)
                "ngo" -> findNavController().navigate(R.id.action_login_to_ngo_dashboard)
                "admin" -> findNavController().navigate(R.id.action_login_to_admin_dashboard)
            }
        } else {
            Toast.makeText(context, "Invalid credentials. Use demo accounts.", Toast.LENGTH_LONG).show()
        }
    }
}

