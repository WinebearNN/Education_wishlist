package com.hse.education.data.repository

import android.util.Log
import com.hse.education.data.datasource.user.LocalDataSourceUser
import com.hse.education.data.datasource.user.RemoteDataSourceUser
import com.hse.education.domain.entity.User
import com.hse.education.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSourceUser: RemoteDataSourceUser,
    private val localDataSourceUser: LocalDataSourceUser
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    override suspend fun getUser(): Result<User> {
        localDataSourceUser.getUser(1)?.let { user: User ->
            return Result.success(user)
        }
        return Result.failure(Exception("User not found"))
    }

    override suspend fun updateUserData(user: User): Result<Unit> {
        val result=remoteDataSourceUser.updateUserData(user)
        if (result.isSuccess) localDataSourceUser.updateUserData(user)
        return result
    }

    override suspend fun uploadImageToServer(globalId:String,array: ByteArray): Result<Unit> {
        return remoteDataSourceUser.uploadImageToServer(globalId,array)
    }

    override suspend fun refreshUserData(): Result<User> {
        val user=localDataSourceUser.getAllUsers()!!.last()
        val result=remoteDataSourceUser.getUserById(user.globalId.toString())
        if(result.isSuccess) localDataSourceUser.updateUserData(result.getOrThrow())
        return result
    }

    override suspend fun logout() {
        localDataSourceUser.removeAll()
    }


    override suspend fun register(user: User): Result<Unit> {
        val result = remoteDataSourceUser.registerUser(user)
        if (result.isSuccess) {
            user.globalId=result.getOrNull()!!.toLong()
            Log.i(TAG,"GlobalId is ${user.globalId}")
            localDataSourceUser.saveUser(user)
            return Result.success(Unit)
        }
        return Result.failure(Exception(result.getOrNull()))
    }

    override suspend fun loginUserByEmail(user: User): Result<User> {
        val result = remoteDataSourceUser.getUserByEmail(user)
        if (result.isSuccess) {
            result.getOrNull()
                ?.let { localDataSourceUser.saveUser(it) }
        }
        return result
    }

    override suspend fun authUser(): Result<User> {
        val users=localDataSourceUser.getAllUsers()!!
        return if (users.isNotEmpty()){
            Result.success(users.last())
        }else{
            Result.failure(Exception("User not auth before"))

        }
    }
}