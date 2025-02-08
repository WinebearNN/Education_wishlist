package com.hse.education.domain.repository

import com.hse.education.domain.entity.User

interface UserRepository {
    suspend fun register(user: User): Result<Unit>
    suspend fun loginUserByEmail(user: User): Result<User>
    suspend fun authUser(): Result<User>
    suspend fun getUser(): Result<User>
    suspend fun updateUserData(user: User): Result<Unit>
    suspend fun uploadImageToServer(globalId:String,array: ByteArray):Result<Unit>
    suspend fun refreshUserData(): Result<User>
    suspend fun logout()
}