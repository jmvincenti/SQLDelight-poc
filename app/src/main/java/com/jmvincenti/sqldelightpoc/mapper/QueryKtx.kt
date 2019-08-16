package com.jmvincenti.sqldelightpoc.mapper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.squareup.sqldelight.Query
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

fun <T : Any> Query<T>.asLiveData(): LiveData<Query<T>> =
    object : LiveData<Query<T>>(), Query.Listener {

        override fun queryResultsChanged() {
            postValue(this@asLiveData)
        }

        override fun onActive() {
            addListener(this)
            postValue(this@asLiveData)
        }

        override fun onInactive() {
            removeListener(this)
        }
    }

private val ioExecutor: Executor by lazy {
    val ioExecutor = ThreadPoolExecutor(
        2,
        10,
        10,
        TimeUnit.SECONDS,
        LinkedBlockingQueue<Runnable>()
    )
    ioExecutor
}

fun <T : Any> LiveData<Query<T>>.mapToOne(
    executor: Executor = ioExecutor
): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { query ->
        executor.execute {
            query.execute().use {
                if (it.next()) {
                    result.postValue(query.mapper(it))
                }
            }
        }
    }
    return result
}

fun <T : Any> LiveData<Query<T>>.mapToOneOrNull(
    executor: Executor = ioExecutor
): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { query ->
        executor.execute {
            query.execute().use {
                result.postValue(if (it.next()) query.mapper(it) else null)
            }
        }
    }
    return result
}

fun <T : Any> LiveData<Query<T>>.mapToList(
    executor: Executor = ioExecutor
): LiveData<List<T>> {
    val result = MediatorLiveData<List<T>>()
    result.addSource(this) { query ->
        executor.execute {
            result.postValue(query.executeAsList())
        }
    }
    return result
}
