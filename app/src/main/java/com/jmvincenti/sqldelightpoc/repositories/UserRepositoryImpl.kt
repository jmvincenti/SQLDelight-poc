package com.jmvincenti.sqldelightpoc.repositories

import androidx.lifecycle.LiveData
import com.jmvincenti.sqldelightpoc.domain.User
import com.jmvincenti.sqldelightpoc.domain.UserRepository
import com.jmvincenti.sqldelightpoc.mapper.USER_MAPPER
import com.jmvincenti.sqldelightpoc.mapper.asLiveData
import com.jmvincenti.sqldelightpoc.mapper.mapToList
import com.jmvincenti.sqldelightpoc.model.UserQueries

class UserRepositoryImpl(private val userQueries: UserQueries) : UserRepository {

    override val userListLiveData: LiveData<List<User>> =
        userQueries.selectAll(mapper = USER_MAPPER)
            .asLiveData()
            .mapToList()

    override fun addUser(user: User) {
        userQueries.insertUser(
            id = user.id,
            first_name = user.firstName,
            last_name = user.lastName,
            thumbnail = user.thumbnail
        )
    }
}
