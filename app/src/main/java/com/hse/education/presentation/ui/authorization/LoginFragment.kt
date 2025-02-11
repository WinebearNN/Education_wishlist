package com.hse.education.presentation.ui.authorization

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.hse.education.R
import com.hse.education.databinding.FragmentLoginBinding
import com.hse.education.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {
        private const val TAG = "LoginFragment"
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        setupUI()

        setupObservers()

        return binding.root
    }


    private fun setupUI() {
        binding.fragmentLoginButtonConfirm.setOnClickListener {

            clearErrors()

            val email = binding.fragmentLoginEditTextEmail.text.toString()
            val password = binding.fragmentLoginEditTextPassword.text.toString()

            viewModel.signInUser(email, password)
        }
    }

    private fun setupObservers() {
        viewModel.globalErrors.observe(viewLifecycleOwner) { errors ->

            for (error in errors) {
                when (error.code) {

                    100 -> {
                        binding.fragmentLoginEditTextEmail.error = error.description
                        binding.fragmentLoginEditTextEmail.setBackgroundResource(R.drawable.edit_text_background_error)
                    }

                    101 -> {
                        binding.fragmentLoginEditTextPassword.error = error.description
                        binding.fragmentLoginEditTextPassword.setBackgroundResource(R.drawable.edit_text_background_error)
                    }

                }
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Log.i(TAG, "Sign in Successful")
                Toast.makeText(requireContext(), "Sign in Successful", Toast.LENGTH_SHORT).show()

                Navigation.findNavController(binding.root).navigate(
                    R.id.action_login_to_profile
                )
            }.onFailure { exception ->
                Log.e(TAG, exception.message.toString())
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun clearErrors() {
        binding.fragmentLoginEditTextEmail.error = null
        binding.fragmentLoginEditTextEmail.error = null
        binding.fragmentLoginEditTextEmail.setBackgroundResource(R.drawable.edit_text_background_white)
        binding.fragmentLoginEditTextPassword.setBackgroundResource(R.drawable.edit_text_background_white)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}