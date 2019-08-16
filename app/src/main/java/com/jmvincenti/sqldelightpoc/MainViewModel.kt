package com.jmvincenti.sqldelightpoc

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.jmvincenti.sqldelightpoc.domain.Content
import com.jmvincenti.sqldelightpoc.domain.ContentRepository

class MainViewModel(contentRepository: ContentRepository) {

    private val userIdLiveData = MutableLiveData<String>()
    private val contentByUserLiveData = Transformations.switchMap(userIdLiveData) {
        contentRepository.getContentByUserId(it)
    }

    private val contentIdLiveData = MutableLiveData<String>()
    private val contentByContentLiveData = Transformations.switchMap(contentIdLiveData) {
        contentRepository.getContentByContentId(it)
    }

    val contentLiveData: LiveData<Content> = MediatorLiveData<Content>().also {
        it.addSource(contentByUserLiveData) { content ->
            Log.v("ContentLD", "Content by user")
            it.value = content
        }

        it.addSource(contentByContentLiveData) { content ->
            Log.v("ContentLD", "Content by content")
            it.value = content
        }
    }


    fun selectUserId(userid: String) {
        userIdLiveData.value = userid
    }

    fun selectContentId(contentId: String) {
        contentIdLiveData.value = contentId
    }
}
