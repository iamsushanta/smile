package iamzen.`in`.smile.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import iamzen.`in`.smile.model.Post
import iamzen.`in`.smile.model.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@DelicateCoroutinesApi
class PostDao {

    private val db = FirebaseFirestore.getInstance()
    var collection = db.collection("post")
    private val auth = Firebase.auth


    fun addPost(text: String){
        val currentUser = auth.currentUser!!.uid
        GlobalScope.launch(Dispatchers.IO) {
            val userDao = UserDao()
            val user = userDao.getUserById(currentUser).await().toObject(User::class.java)!!
            val userTime = System.currentTimeMillis()
            val post = Post(text,user,userTime)
            collection.document().set(post)
        }
    }

    private fun getPostById(uid: String): Task<DocumentSnapshot>{
        return collection.document(uid).get()
    }
    fun updateLike(uid:String){
        GlobalScope.launch(Dispatchers.IO) {
            val currentUser = auth.currentUser!!.uid
            val post = getPostById(uid).await().toObject(Post::class.java)!!
            val isLike = post.like.contains(currentUser)
            
            if(isLike){
                post.like.remove(currentUser)
            } else{
                post.like.add(currentUser)
            }

            collection.document(uid).set(post)
        }
    }
}