package com.jmvincenti.sqldelightpoc.domain

import androidx.lifecycle.LiveData

interface ContentRepository {

    fun getContentByUserId(userId: String): LiveData<Content>
    fun getContentByContentId(contentId: String): LiveData<Content>

    fun addContent(content: Content)
}

data class Content(
    val id: String,
    val title: String?,
    val body: String?,
    val author: User
)
