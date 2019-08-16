package com.jmvincenti.sqldelightpoc

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jmvincenti.sqldelightpoc.domain.Content
import com.jmvincenti.sqldelightpoc.domain.ContentRepository
import com.jmvincenti.sqldelightpoc.domain.User
import com.jmvincenti.sqldelightpoc.domain.UserRepository
import com.jmvincenti.sqldelightpoc.repositories.ContentRepositoryImpl
import com.jmvincenti.sqldelightpoc.repositories.UserRepositoryImpl
import com.squareup.sqldelight.android.AndroidSqliteDriver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Math.random


class MainActivity : AppCompatActivity() {

    lateinit var userRepository: UserRepository
    lateinit var contentRepository: ContentRepository
    lateinit var mainViewModel: MainViewModel
    lateinit var database: PocDatabase

    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            addNewContent()
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, mutableListOf())
        user_list.adapter = adapter
        user_list.setOnItemClickListener { _, _, position, _ ->
            userRepository.userListLiveData.value?.get(position)?.id?.let {
                mainViewModel.selectUserId(it)
            }
        }

        injectRepositories()

        userRepository.userListLiveData.observe(this, Observer { users: List<User> ->
            adapter.clear()
            adapter.addAll(users.map { it.toString() })
        })

        mainViewModel.contentLiveData.observe(this, Observer { content ->
            contentView.text = content.toString()
        })
    }


    private fun injectRepositories() {
        val driver = AndroidSqliteDriver(PocDatabase.Schema, applicationContext, "User.db")
        database = PocDatabase(driver)

        userRepository = UserRepositoryImpl(database.userQueries)
        contentRepository = ContentRepositoryImpl(database, database.contentQueries, userRepository)

        mainViewModel = MainViewModel(contentRepository)
    }

    private fun addNewContent() {
        val user = User(
                id = System.currentTimeMillis().toString(),
                firstName = "Joe",
                lastName = ((random() * 1000).toInt()).toString(),
                thumbnail = null
        )

        val content = Content(
                id = System.currentTimeMillis().toString(),
                title = "Title:${((random() * 1000).toInt())}",
                body = "UserLastName expected:${user.lastName}",
                author = user
        )
        contentRepository.addContent(content)
        mainViewModel.selectContentId(content.id)
    }
}
