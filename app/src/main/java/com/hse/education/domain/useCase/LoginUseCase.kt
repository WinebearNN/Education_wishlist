package com.hse.education.domain.useCase

import com.hse.education.domain.entity.User
import com.hse.education.domain.repository.UserRepository
import com.hse.education.utills.CheckValidation.Companion.isValidEmail
import com.hse.education.utills.CheckValidation.Companion.isValidPassword
import com.hse.education.utills.ErrorCode
import com.hse.education.utills.ErrorException
import com.hse.education.utills.GlobalError
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val userRepository: UserRepository) {

    companion object {
        private const val TAG = "LoginUserUseCase"
    }

    suspend fun execute(user: User): Result<User> {
        val globalErrors = mutableListOf<GlobalError>()

        validateUser(user, globalErrors)

        if (globalErrors.isNotEmpty()) {
            return Result.failure(ErrorException(globalErrors))
        }

        return userRepository.loginUserByEmail(user)
    }

    private fun validateUser(user: User, globalErrors: MutableList<GlobalError>) {
        if (!isValidEmail(user.email)) {
            globalErrors.add(GlobalError(100, ErrorCode.CODE_100.description))
        }

        if (!isValidPassword(user.password)) {
            globalErrors.add(GlobalError(101, ErrorCode.CODE_101.description))
        }
    }


}