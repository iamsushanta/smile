package iamzen.`in`.smile.daos

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import iamzen.`in`.smile.model.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class UserDao{

    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")


    fun addUser(user: User?){
        user?.let {
            GlobalScope.launch(Dispatchers.IO){
                userCollection.document(user.userUID).set(it)
            }
        }
    }

    fun getUserById(uid: String): Task<DocumentSnapshot> {
        return userCollection.document(uid).get()
    }


}