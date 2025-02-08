package com.hse.education.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hse.education.domain.entity.User
import com.hse.education.domain.useCase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val _loadingAvatar = MutableLiveData<Boolean>()
    val loadingAvatar: LiveData<Boolean> get() = _loadingAvatar

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _logoutFlag = MutableLiveData<Boolean>()
    val logoutFlag:LiveData<Boolean> get()=_logoutFlag



    private val _linkFlag = MutableLiveData<Boolean>()
    val linkFlag: LiveData<Boolean> get() = _linkFlag

    init {
        loadLocalUserData()
        refreshUserData()
    }

    fun refreshUserData() {
        viewModelScope.launch {
            runCatching {
                profileUseCase.refreshUserData()
            }.onSuccess { result ->
                if (_logoutFlag.value == true) return@onSuccess
                _user.postValue(result.getOrThrow())
            }.onFailure { exception ->
                Log.e(TAG, "Error loading user data from server: $exception")
            }
        }
    }

    private fun loadLocalUserData() {
        viewModelScope.launch {
            runCatching {
                profileUseCase.getUser()
            }.onSuccess { localResult ->
                // Проверяем, если уже идет процесс logout, то не обновляем _user
                if (_logoutFlag.value == true) return@onSuccess
                _user.postValue(localResult.getOrThrow()) // Используем postValue для потокобезопасности
            }.onFailure { exception ->
                Log.e(TAG, "Error loading user data: $exception")
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            _logoutFlag.postValue(true)
            profileUseCase.logout()
            Log.d(TAG,"Logout")
            _logoutFlag.postValue(false)
        }
    }

    fun updateUserData(user: User) {
        viewModelScope.launch {
            _loadingAvatar.postValue(true)
            runCatching {
                profileUseCase.updateUserData(user)
            }.onFailure { exception ->
                Log.e(TAG, "Failed to update user data: $exception")
            }
            _loadingAvatar.postValue(false)
        }
    }

    fun uploadProfileImage(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val imageStream = context.contentResolver.openInputStream(imageUri)
                val imageBytes = imageStream?.readBytes()
                imageStream?.close()

                if (imageBytes != null) {
                    val result = profileUseCase.uploadImageToServer(
                        user.value?.globalId.toString(),
                        imageBytes
                    )
                    Log.i(TAG, result.toString())
                    _loadingAvatar.postValue(result.isSuccess)
                } else {
                    _loadingAvatar.postValue(false)
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error uploading image: ${e.message}")
                _loadingAvatar.postValue(false)
            }
        }
    }
}
