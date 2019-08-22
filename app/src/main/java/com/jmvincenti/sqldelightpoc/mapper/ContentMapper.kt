package com.jmvincenti.sqldelightpoc.mapper

import com.jmvincenti.sqldelightpoc.domain.Content
import com.jmvincenti.sqldelightpoc.domain.LocalizedString
import com.jmvincenti.sqldelightpoc.domain.User

val CONTENT_FULL_MAPPER: (String, String?, LocalizedString?, String?, String?, String?) -> Content =
        { contentId, contentTitle, contentBody, userId, userFirstName, userLastName ->
            Content(
                    id = contentId,
                    title = contentTitle,
                    body = contentBody,
                    author = User(
                            id = userId!!,
                            firstName = userFirstName!!,
                            lastName = userLastName!!
                    )
            )
        }
