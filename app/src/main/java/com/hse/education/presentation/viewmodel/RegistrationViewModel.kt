package com.hse.education.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hse.education.domain.entity.User
import com.hse.education.domain.useCase.RegistrationUseCase
import com.hse.education.utills.ErrorException
import com.hse.education.utills.GlobalError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "RegistrationViewModel"
    }

    private val _globalErrors = MutableLiveData<List<GlobalError>>()
    val globalErrors: LiveData<List<GlobalError>> = _globalErrors

    private val _registrationResult = MutableLiveData<Result<Unit>>()
    val registrationResult: LiveData<Result<Unit>> = _registrationResult

    fun registerUser(email: String, password: String, name: String) {
        val user = User(email = email, password = password, userName = name)

        viewModelScope.launch {
            val result = registrationUseCase.execute(user)
            handleRegistrationResult(result)
        }
    }

    private fun handleRegistrationResult(result: Result<Unit>) {
        result.onSuccess {
            _registrationResult.value = Result.success(Unit)
        }.onFailure { exception ->
            if (exception is ErrorException) {
                _globalErrors.value = exception.errors
            } else {
                _registrationResult.value = Result.failure(exception)
            }
        }
    }
}