package com.hse.education.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hse.education.domain.entity.User
import com.hse.education.domain.useCase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val _authResult = MutableLiveData<Result<User>>()
    val authResult: LiveData<Result<User>> = _authResult

    fun authUser() {

        viewModelScope.launch {
            val result = authUseCase.execute()
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: Result<User>) {
        result.onSuccess {
            _authResult.value = Result.success(result.getOrNull()!!)
        }.onFailure { exception ->
            _authResult.value = Result.failure(exception)
        }

    }
}