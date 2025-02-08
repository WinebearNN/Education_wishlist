package com.hse.education.data.network.apiService

import com.hse.education.data.network.ApiResponse
import com.hse.education.data.network.RegisterUserRequest
import com.hse.education.domain.entity.User
import dagger.Binds
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceUser {

    @POST("/user/registration/")
    suspend fun registerUser(@Body request: RegisterUserRequest): ApiResponse

    @GET("/user/get/email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): ApiResponse

    @POST("/user/update/")
    suspend fun updateUserData(@Body user: User): ApiResponse

    @POST("/user/avatar/upload/{id}")
    suspend fun uploadAvatarToServer(@Path("id") globalId:String, @Body array: ByteArray): ApiResponse

    @GET("/user/get/id/{id}")
    suspend fun getUserById(@Path("id") globalId:String): ApiResponse
}