package com.jmvincenti.sqldelightpoc.repositories

import androidx.lifecycle.LiveData
import com.jmvincenti.sqldelightpoc.PocDatabase
import com.jmvincenti.sqldelightpoc.domain.Content
import com.jmvincenti.sqldelightpoc.domain.ContentRepository
import com.jmvincenti.sqldelightpoc.domain.UserRepository
import com.jmvincenti.sqldelightpoc.mapper.CONTENT_FULL_MAPPER
import com.jmvincenti.sqldelightpoc.mapper.asLiveData
import com.jmvincenti.sqldelightpoc.mapper.mapToOne
import com.jmvincenti.sqldelightpoc.model.ContentQueries

class ContentRepositoryImpl(
    private val database: PocDatabase,
    private val contentQueries: ContentQueries,
    private val userRepository: UserRepository
) : ContentRepository {
    override fun getContentByContentId(contentId: String): LiveData<Content> =
        contentQueries.selectContentByContentId(contentId, CONTENT_FULL_MAPPER)
            .asLiveData()
            .mapToOne()

    override fun getContentByUserId(userId: String): LiveData<Content> =
        contentQueries.selectContentByUserId(userId, CONTENT_FULL_MAPPER)
            .asLiveData()
            .mapToOne()

    override fun addContent(content: Content) {
        database.transaction {
            contentQueries.insertContent(content.id, content.title, content.body, content.author.id)
            userRepository.addUser(content.author)
        }
    }
}
