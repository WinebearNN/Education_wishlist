package com.hse.education.presentation.ui.general

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hse.education.R
import com.hse.education.databinding.FragmentGeneralBinding


class GeneralFragment : Fragment() {

    companion object {
        private const val TAG = "GeneralFragment"
    }

    private var _binding:FragmentGeneralBinding? =null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentGeneralBinding.inflate(inflater,container,false)




        return binding.root
    }


}