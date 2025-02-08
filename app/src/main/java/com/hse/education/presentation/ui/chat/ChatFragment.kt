package com.hse.education.presentation.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hse.education.databinding.FragmentChatBinding


class ChatFragment : Fragment() {

    companion object {
        private const val TAG="ChatFragment"
    }

    private var _binding:FragmentChatBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentChatBinding.inflate(inflater,container,false)



        return binding.root
    }


}