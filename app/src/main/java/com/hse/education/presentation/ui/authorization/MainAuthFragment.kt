package com.hse.education.presentation.ui.authorization

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.hse.education.R
import com.hse.education.databinding.FragmentMainAuthBinding
import com.hse.education.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainAuthFragment : Fragment() {

    companion object {
        private const val TAG = "MainAuthFragment"
    }

    private val viewModel:AuthViewModel by viewModels()

    private var _binding: FragmentMainAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainAuthBinding.inflate(inflater, container, false)

        viewModel.authUser()
        setupObservers(binding.root)

        binding.fragmentMainAuthButtonSignIn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(
                R.id.action_mainAuth_to_login
            )
        }
        binding.fragmentMainAuthButtonRegister.findViewById<Button>(R.id.fragment_main_auth_button_register).setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_mainAuth_to_registration)
        }

        return binding.root
    }


    private fun setupObservers(view:View) {
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { user ->
                Toast.makeText(requireContext(), "Hello, ${user.userName}", Toast.LENGTH_LONG).show()
                Log.i("MainAuth", "Authentication successful: ${user.userName}")
                Navigation.findNavController(view)
                    .navigate(R.id.action_mainAuth_to_profile)

            }.onFailure { exception ->
                Log.e("MainActivity", exception.message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}