package com.hse.education.presentation.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hse.education.R
import com.hse.education.databinding.FragmentProfileEditBinding
import com.hse.education.domain.entity.User
import com.hse.education.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {
    companion object{
        private const val TAG="ProfileEditFragment"
    }

    private var _binding:FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private val viewModelP: ProfileViewModel by activityViewModels()

    private val updateObserver = androidx.lifecycle.Observer<Boolean> { isProcess ->
        if(isProcess) {
            //TODO animation
        }else{
            findNavController().navigate(R.id.action_profileEdit_to_profile)
        }
    }

    private fun initUI(){
        val currentUser = viewModelP.user.value ?: throw Exception("Непредвиденная ошибка")

        binding.editTextUserPassword.setText(currentUser.password)
        binding.editTextUserLink.setText(currentUser.link)
        binding.editTextUserName.setText(currentUser.userName)
        binding.editTextUserEmail.setText(currentUser.email)
        binding.buttonSave.setOnClickListener {
            val updatedUser = currentUser.copy(
                userName = binding.editTextUserName.text.toString(),
                email = binding.editTextUserEmail.text.toString(),
                password = binding.editTextUserPassword.text.toString(),
                link = binding.editTextUserLink.text.toString()
            )
            if (updatedUser != currentUser) {
                viewModelP.updateUserData(updatedUser)
                viewModelP.loading.observe(viewLifecycleOwner, updateObserver)
            } else {
                Log.d(TAG,"Нет изменений для сохранения")
                findNavController().navigate(R.id.action_profileEdit_to_profile)
            }
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentProfileEditBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        Log.i(TAG, binding.editTextUserPassword.height.toString())


    }



    override fun onDestroyView() {
        super.onDestroyView()
        viewModelP.loading.removeObserver(updateObserver)
        _binding=null
    }
}