package com.jmvincenti.sqldelightpoc.mapper

import com.jmvincenti.sqldelightpoc.domain.User

val USER_MAPPER: (String, String, String, String?, Boolean?) -> User =
    { id, first_name, last_name, thumbnail, _ ->
        User(id, first_name, last_name, thumbnail)
    }
