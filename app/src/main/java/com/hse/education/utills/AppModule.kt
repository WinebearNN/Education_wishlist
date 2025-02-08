package com.hse.education.utills

import android.content.Context
import android.util.Log
import com.getkeepsafe.relinker.ReLinker
import com.hse.education.data.datasource.user.LocalDataSourceUser
import com.hse.education.data.datasource.user.RemoteDataSourceUser
import com.hse.education.data.network.apiService.ApiServiceProvider
import com.hse.education.data.network.apiService.ApiServiceUser
import com.hse.education.data.repository.UserRepositoryImpl
import com.hse.education.domain.entity.MyObjectBox
import com.hse.education.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }


    @Provides
    @Singleton
    fun provideApiServiceUser(): ApiServiceUser {
        return ApiServiceProvider.apiServiceUser
    }


    @Provides
    @Singleton
    fun provideRemoteDataSourceUser(apiServiceUser: ApiServiceUser): RemoteDataSourceUser {
        return RemoteDataSourceUser(apiServiceUser)  // Предоставляем RemoteDataSource
    }

    @Provides
    @Singleton
    fun provideLocalDataSourceUser(boxStore: BoxStore): LocalDataSourceUser {
        return LocalDataSourceUser(boxStore)
    }


    @Provides
    @Singleton
    fun provideUserRepository(
        remoteDataSourceUser: RemoteDataSourceUser,
        localDataSourceUser: LocalDataSourceUser
    ): UserRepository {
        return UserRepositoryImpl(
            remoteDataSourceUser,
            localDataSourceUser
        )
    }


    @Provides
    @Singleton
    fun provideBoxStore(@ApplicationContext context: Context): BoxStore {
        val boxStore = MyObjectBox.builder()
            .androidContext(context)
            .androidReLinker(ReLinker.log(object : ReLinker.Logger {
                override fun log(message: String) {
                    Log.d("ObjectBoxLog", message)
                }
            }))
            .build()

//        boxStore.close()
//        boxStore.deleteAllFiles()

        return boxStore;


    }
}
