package com.hse.education.data.datasource.user

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hse.education.data.network.RegisterUserRequest
import com.hse.education.data.network.apiService.ApiServiceUser
import com.hse.education.domain.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSourceUser @Inject constructor(
    private val apiServiceUser: ApiServiceUser
) {
    private val gson: Gson = GsonBuilder()
        .create()

    companion object {
        private const val TAG = "RemoteDataSourceUser"
    }

    suspend fun registerUser(user: User): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val request = RegisterUserRequest(
                email = user.email,
                password = user.password,
                userName = user.userName
            )
            Log.i(TAG, "User for request $request")
            val response = apiServiceUser.registerUser(request)
            if (response.success) response.message else throw Exception("Registration failed: ${response.message}")
        }.onFailure { e ->
            Log.e(TAG, "An error occurred during registration", e)
        }
    }

    suspend fun updateUserData(user: User): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val response = apiServiceUser.updateUserData(user)
            Log.d(TAG,response.message)
            if (response.success) Unit
            else throw Exception("Failed to update user: ${response.message}")
        }.onSuccess {
            Log.d(TAG, "User updated successfully")
        }.onFailure { e ->
            Log.e(TAG, "An error occurred while fetching the user", e)
        }
    }

    suspend fun getUserByEmail(user: User): Result<User> = withContext(Dispatchers.IO) {
        runCatching {
            val response = apiServiceUser.getUserByEmail(user.email)
            if (response.success) {
                val userResponse = gson.fromJson(response.message, User::class.java)
                if (user.password == userResponse.password) userResponse
                else throw Exception("Invalid password")
            } else {
                throw Exception("Failed to get user: ${response.message}")
            }
        }.onSuccess {
            Log.d(TAG, "User fetched successfully: ${it.email}")
        }.onFailure { e ->
            Log.e(TAG, "An error occurred while fetching the user", e)
        }
    }

    suspend fun uploadImageToServer(globalId: String, array: ByteArray): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = apiServiceUser.uploadAvatarToServer(globalId, array)
                if (response.success) Unit
                else {
                    throw Exception("Failed to upload user's avatar: ${response.message}")
                }
            }.onSuccess {
                Log.d(TAG, "User's avatar updated successfully")
            }.onFailure { e ->
                Log.e(TAG, "An error occurred while updating the user's avatar", e)
            }
        }

    suspend fun getUserById(globalId: String): Result<User> = withContext(Dispatchers.IO) {
        runCatching {
            val response = apiServiceUser.getUserById(globalId)
            if (response.success) {
                val userResponse = gson.fromJson(response.message, User::class.java)
                userResponse
            } else {
                throw Exception("Failed to get user by id:${response.message}")
            }
        }.onFailure { e ->
            Log.e(TAG, "An error occurred while getting user by id", e)
        }

    }


}