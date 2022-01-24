package iamzen.`in`.smile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import iamzen.`in`.smile.daos.PostDao
import kotlinx.android.synthetic.main.activity_post_created.*
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class PostCreated : AppCompatActivity() {

    lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_created)

        postDao = PostDao()

        newPostButton.setOnClickListener{
            val inputNewStory = newStory.text.toString().trim()
            if(inputNewStory.isNotEmpty()){
                postDao.addPost(inputNewStory)
                finish()
            }
        }
    }
}