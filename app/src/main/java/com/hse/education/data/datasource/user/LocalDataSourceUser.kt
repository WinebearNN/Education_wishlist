package com.hse.education.data.datasource.user

import android.util.Log
import com.hse.education.domain.entity.User
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Inject

class LocalDataSourceUser @Inject constructor(
    boxStore: BoxStore
) {
    private val userBox: Box<User> = boxStore.boxFor(User::class.java)

    companion object {
        private const val TAG = "LocalDataSourceUser"
    }

    fun removeAll(){
        userBox.removeAll()
    }

    fun updateUserData(user: User){
        userBox.removeAll()
        userBox.put(user)
    }
    fun getAllUsers(): MutableList<User>? {
        Log.i(TAG,"All users: ${userBox.all}");
        return userBox.all
    }

    fun saveUser(user:User){
        userBox.put(user)
        Log.i(TAG,"User was saved: $user")
    }

    fun removeUser(user: User){
        Log.i(TAG,"User was removed: $user")
        userBox.remove(user)
    }

    fun getUser(id:Long):User?{
        Log.i(TAG,"User was taken: ${userBox.get(id)}")
        return userBox.get(id)
    }

    fun contains(id:Long):Boolean{
        Log.i(TAG,"Is user contains: ${userBox.contains(id)}")
        return userBox.contains(id)
    }




}