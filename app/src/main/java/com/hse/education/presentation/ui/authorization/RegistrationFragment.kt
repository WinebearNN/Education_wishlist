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
import com.hse.education.databinding.FragmentRegistrationBinding
import com.hse.education.presentation.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    companion object {
        private const val TAG = "RegistrationFragment"
    }

    private val viewModel: RegistrationViewModel by viewModels()

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        setupUI()
        setupObservers()

        return binding.root
    }

    private fun setupUI() {
        binding.fragmentRegistrationButtonConfirm.setOnClickListener {
            clearErrors()

            val email = binding.fragmentRegistrationEditTextEmail.text.toString()
            val password = binding.fragmentRegistrationEditTextPassword.text.toString()
            val name = binding.fragmentRegistrationEditTextName.text.toString()

            viewModel.registerUser(email, password, name)
        }
    }

    private fun setupObservers() {
        viewModel.globalErrors.observe(viewLifecycleOwner) { errors ->
            errors?.let {
                for (error in it) {
                    when (error.code) {
                        100 -> {
                            binding.fragmentRegistrationEditTextEmail.error = error.description
                            binding.fragmentRegistrationEditTextEmail.setBackgroundResource(R.drawable.edit_text_background_error)
                        }

                        101 -> {
                            binding.fragmentRegistrationEditTextPassword.error = error.description
                            binding.fragmentRegistrationEditTextPassword.setBackgroundResource(R.drawable.edit_text_background_error)
                        }

                        102 -> {
                            binding.fragmentRegistrationEditTextName.error = error.description
                            binding.fragmentRegistrationEditTextName.setBackgroundResource(R.drawable.edit_text_background_error)
                        }
                    }
                }
            }
        }

        viewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT)
                    .show()
                Log.i(TAG, "Registration successful")
                Navigation.findNavController(binding.root).navigate(
                    R.id.action_registration_to_profile
                )
            }.onFailure { exception ->
                Log.e(TAG, exception.message.toString());
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun clearErrors() {
        binding.fragmentRegistrationEditTextPassword.error = null
        binding.fragmentRegistrationEditTextEmail.error = null
        binding.fragmentRegistrationEditTextName.error = null
        binding.fragmentRegistrationEditTextPassword.setBackgroundResource(R.drawable.edit_text_background_white)
        binding.fragmentRegistrationEditTextEmail.setBackgroundResource(R.drawable.edit_text_background_white)
        binding.fragmentRegistrationEditTextName.setBackgroundResource(R.drawable.edit_text_background_white)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}