package com.hse.education.domain.useCase

import com.hse.education.domain.entity.User
import com.hse.education.domain.repository.UserRepository
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val userRepository: UserRepository) {
    companion object{
        private const val TAG="ProfileUseCase"
    }

    suspend fun getUser(): Result<User> {
        return userRepository.authUser()
    }

    suspend fun refreshUserData(): Result<User> {
        return userRepository.refreshUserData()
    }

    suspend fun logout() {
       return userRepository.logout()
    }

    suspend fun updateUserData(user: User): Result<Unit> {
        return userRepository.updateUserData(user)
    }

    suspend fun uploadImageToServer(globalId:String,array: ByteArray): Result<Unit>{
        return userRepository.uploadImageToServer(globalId,array)
    }
}