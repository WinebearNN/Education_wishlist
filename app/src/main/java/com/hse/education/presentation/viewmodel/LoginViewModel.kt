package com.hse.education.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hse.education.domain.entity.User
import com.hse.education.domain.useCase.LoginUseCase
import com.hse.education.utills.ErrorException
import com.hse.education.utills.GlobalError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _globalErrors = MutableLiveData<List<GlobalError>>()
    val globalErrors: LiveData<List<GlobalError>> = _globalErrors

    private val _loginResult = MutableLiveData<Result<Unit>>()
    val loginResult: LiveData<Result<Unit>> = _loginResult

    fun signInUser(email: String, password: String) {
        val user = User(email = email, password = password)

        viewModelScope.launch {
            val result = loginUseCase.execute(user)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: Result<User>) {
        result.onSuccess {
            _loginResult.value = Result.success(Unit)
        }.onFailure { exception ->
            if (exception is ErrorException) {
                _globalErrors.value = exception.errors
            } else {
                _loginResult.value = Result.failure(exception)
            }
        }
    }

}