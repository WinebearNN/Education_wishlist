package com.hse.education.presentation.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hse.education.R

import com.hse.education.databinding.FragmentProfileBinding
import com.hse.education.domain.entity.Interest
import com.hse.education.domain.entity.User
import com.hse.education.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    companion object {
        private const val TAG = "ProfileFragment"
        private const val PICK_IMAGE_REQUEST = 100
    }

    private val viewModelP: ProfileViewModel by activityViewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userObserver = androidx.lifecycle.Observer<User?> { user ->
        user?.let {
            updateUI(it)
        }
    }

    private val logoutObserver = androidx.lifecycle.Observer<Boolean> { isProcess ->
        if(isProcess) {
            viewModelP.loading.removeObserver(avatarObserver)
            viewModelP.user.removeObserver(userObserver)
        }else{
            findNavController().navigate(R.id.action_profile_to_mainAuth)
        }
    }

    private val avatarObserver = androidx.lifecycle.Observer<Boolean>{ isSuccess->
        if (isSuccess) {
            Toast.makeText(requireContext(), "Фото успешно загружено!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Ошибка загрузки фото!", Toast.LENGTH_SHORT).show()
        }
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()



        initUI()


    }

    private fun observeViewModel(){
        // Подписка на изменение данных пользователя
        viewModelP.user.observe(viewLifecycleOwner,userObserver)

        viewModelP.logoutFlag.observe(viewLifecycleOwner,logoutObserver)


//        viewModelP.loadingAvatar.observe(viewLifecycleOwner) { isSuccess ->
//            if(isSuccess){
//                findNavController().navigate(R.id.action_profile_to_mainAuth)
//            } else{
//                Toast.makeText(requireContext(), "Ошибка при выходе из аккаунта!", Toast.LENGTH_LONG).show()
//            }
//        }



        // Подписка на статус загрузки
        viewModelP.loading.observe(viewLifecycleOwner,avatarObserver)
    }

    private fun initUI() {
        binding.imageViewAvatar.setOnClickListener {
            checkPermissionsAndOpenGallery() // Теперь метод вызывается при клике
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                loadProfileImage(imageUri)
            }
        }
    }

    private fun loadProfileImage(imageUri: Uri) {
        Glide.with(this)
            .load(imageUri)
            .circleCrop() // Обрезка в круг (можно убрать, если не нужно)
//            .placeholder(R.drawable.placeholder) // Заглушка, пока загружается изображение
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imageViewAvatar)
        viewModelP.uploadProfileImage(imageUri, requireContext()) // Загружаем фото на сервер
    }

    private fun updateUI(user: User) {
        binding.textViewUsername.text = user.userName
        binding.textViewUserEmail.text = user.email
        Glide.with(this)
            .load("http://10.0.2.2:8080/user/avatar/${user.globalId}/")
            .circleCrop() // Обрезка в круг (можно убрать, если не нужно)
            .into(binding.imageViewAvatar)
        binding.buttonEditProfile.setOnClickListener{
            findNavController().navigate(R.id.action_profile_to_profileEdit)
        }

        binding.buttonLogout.setOnClickListener{
            viewModelP.logout()
        }

        Log.d(TAG,"${user.interest}")
        if (user.interest.isNotEmpty()) {
            val interestList = user.interest.toList() // Преобразуем в List для доступа по индексу

            // Заполняем TextView интересами
            if (interestList.isNotEmpty()) {
                binding.constraintLayout1.visibility = View.VISIBLE
                val temp=Interest.fromValue(interestList[0])!!
                binding.textViewInterest1.text = temp.translation
                binding.imageInterest1.setImageResource(temp.imageId)
            }
            if (interestList.size > 1) {
                binding.constraintLayout2.visibility = View.VISIBLE
                val temp=Interest.fromValue(interestList[1])!!
                binding.textViewInterest2.text = temp.translation
                binding.imageInterest2.setImageResource(temp.imageId)
            }
            if (interestList.size > 2) {
                binding.constraintLayout3.visibility = View.VISIBLE
                val temp=Interest.fromValue(interestList[2])!!
                binding.textViewInterest3.text = temp.translation
                binding.imageInterest3.setImageResource(temp.imageId)
            }

            // Скрываем неиспользуемые ConstraintLayouts
            if (interestList.size < 3) binding.constraintLayout3.visibility = View.GONE
            if (interestList.size < 2) binding.constraintLayout2.visibility = View.GONE
            if (interestList.isEmpty()) binding.constraintLayout1.visibility = View.GONE

        } else {
            // Если интересов нет, скрываем все блоки и показываем сообщение
            binding.constraintLayout1.visibility = View.GONE
            binding.constraintLayout2.visibility = View.GONE
            binding.constraintLayout3.visibility = View.GONE
            binding.buttonAllInterests.visibility = View.GONE
            binding.textViewInterestsAbsent.visibility = View.VISIBLE
        }


    }

    private fun checkPermissionsAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), PICK_IMAGE_REQUEST)
            } else {
                openGallery() // Открываем галерею, если разрешение уже есть
            }
        } else {
            openGallery() // Для более старых версий Android
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_IMAGE_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery() // Теперь галерея откроется только после получения разрешения
        } else {
            Toast.makeText(requireContext(), "Разрешение отклонено", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModelP.logoutFlag.removeObserver(logoutObserver)
        Log.i(TAG,"Logout has observer: ${viewModelP.logoutFlag.hasObservers()}")
        Log.i(TAG,"User has observer: ${viewModelP.user.hasObservers()}")
        Log.i(TAG,"Avatar has observer: ${viewModelP.loading.hasObservers()}")
    }
}

