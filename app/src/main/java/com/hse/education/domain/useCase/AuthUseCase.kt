package com.hse.education.domain.useCase

import android.util.Log
import com.hse.education.domain.entity.User
import com.hse.education.domain.repository.UserRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val userRepository: UserRepository) {

    companion object {
        private const val TAG = "AuthUserUseCase"
    }

    suspend fun execute(): Result<User> {
        val result=userRepository.authUser()
        Log.i(TAG,"$result")
        return result
    }

}