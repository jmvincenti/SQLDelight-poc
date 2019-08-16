package com.jmvincenti.sqldelightpoc.domain

import androidx.lifecycle.LiveData

interface UserRepository {

    val userListLiveData: LiveData<List<User>>

    fun addUser(user: User)
}

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val thumbnail: String? = null
)
