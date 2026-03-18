package com.foodbridge.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.foodbridge.app.DummyDataRepository
import com.foodbridge.app.FirebaseDataRepository
import com.foodbridge.app.R
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var selectedRole: String = "all"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedRole = arguments?.getString("selectedRole", "all") ?: "all"

        val loginTitle = view.findViewById<TextView>(R.id.login_title)
        val loginSubtitle = view.findViewById<TextView>(R.id.login_subtitle)
        val emailInput = view.findViewById<EditText>(R.id.login_email_input)
        val passwordInput = view.findViewById<EditText>(R.id.login_password_input)
        val loginButton = view.findViewById<Button>(R.id.login_button)

        applyRoleHeader(loginTitle, loginSubtitle)
        prefillByRole(emailInput, passwordInput)

        loginButton.setOnClickListener {
            performLogin(emailInput.text.toString(), passwordInput.text.toString())
        }
    }

    private fun performLogin(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val account = tryFirebaseLogin(email, password) ?: tryDummyLogin(email, password)

            if (account != null) {
                if (selectedRole != "all" && account.userType != selectedRole) {
                    val mismatchMessage = "Use a ${selectedRole.uppercase()} account from the home menu"
                    Toast.makeText(context, mismatchMessage, Toast.LENGTH_LONG).show()
                    return@launch
                }

                Toast.makeText(context, "Login successful! Welcome", Toast.LENGTH_SHORT).show()

                // Navigate based on user type
                when (account.userType) {
                    "restaurant" -> findNavController().navigate(R.id.action_login_to_restaurant_dashboard)
                    "ngo" -> findNavController().navigate(R.id.action_login_to_ngo_dashboard)
                    "admin" -> findNavController().navigate(R.id.action_login_to_admin_dashboard)
                }
            } else {
                Toast.makeText(
                    context,
                    "Invalid credentials. Create users in Firebase Auth or use demo accounts.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun tryFirebaseLogin(email: String, password: String) = try {
        val ctx = context ?: return null
        if (!FirebaseDataRepository.isConfigured(ctx)) return null
        FirebaseDataRepository.loginAndGetProfile(email, password)
    } catch (_: Exception) {
        null
    }

    private fun tryDummyLogin(email: String, password: String) = DummyDataRepository.dummyAccounts.find {
        it.email == email && it.password == password
    }

    private fun applyRoleHeader(title: TextView, subtitle: TextView) {
        when (selectedRole) {
            "restaurant" -> {
                title.text = "Restaurant Login"
                subtitle.text = "Use restaurant account credentials"
            }

            "ngo" -> {
                title.text = "NGO Login"
                subtitle.text = "Use NGO account credentials"
            }

            "admin" -> {
                title.text = "Admin Login"
                subtitle.text = "Use admin account credentials"
            }

            else -> {
                title.text = "FoodBridge Login"
                subtitle.text = "Sign in to continue"
            }
        }
    }

    private fun prefillByRole(emailInput: EditText, passwordInput: EditText) {
        when (selectedRole) {
            "restaurant" -> emailInput.setText("restaurant@demo.com")
            "ngo" -> emailInput.setText("ngo@demo.com")
            "admin" -> emailInput.setText("admin@demo.com")
            else -> emailInput.setText("")
        }
        passwordInput.setText("demo123")
    }
}

