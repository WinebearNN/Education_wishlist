package com.hse.education.presentation.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hse.education.R
import com.hse.education.databinding.FragmentProfileEditBinding
import com.hse.education.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {

    private var _binding:FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private val viewModelP:ProfileViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentProfileEditBinding.inflate(inflater,container,false)

        return binding.root
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}