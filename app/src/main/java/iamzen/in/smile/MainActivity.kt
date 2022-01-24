package iamzen.`in`.smile

import android.app.DownloadManager
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import iamzen.`in`.smile.daos.PostDao
import iamzen.`in`.smile.daos.UserDao
import iamzen.`in`.smile.model.Post
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.DelicateCoroutinesApi
import androidx.annotation.NonNull
import com.bumptech.glide.Glide

import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.auth.EmailAuthProvider

import com.google.firebase.auth.AuthCredential

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


private const val TAG = "MainActivity"
@DelicateCoroutinesApi
class MainActivity : AppCompatActivity(), IPostLike {

    lateinit var mAdapter: PostAdapter
    lateinit var mPostDao: PostDao
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this,PostCreated::class.java)
            startActivity(intent)
        }

        auth = Firebase.auth
        createRecyclerView()

    }

    private fun createRecyclerView(){
        mPostDao = PostDao()
        val postCollection = mPostDao.collection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val option = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()
        mAdapter = PostAdapter(option,this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter

    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

    override fun postLikeTab(postId: String) {
        mPostDao.updateLike(postId)
    }

    // menu is created down bellow

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.log_out, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.log_out -> {
                val alertDialog: AlertDialog = this.let {
                    val builder = AlertDialog.Builder(it)
                    builder.setTitle("Are you sure logout account")
                    builder.setMessage("you are a logout than you before go that time again sign up")
                    builder.apply {
                        setPositiveButton(R.string.ok,
                            DialogInterface.OnClickListener { dialog, id ->
                                // User clicked OK button
                                Log.d(TAG, "ok this logout")
                                // logout account
                                GlobalScope.launch(Dispatchers.IO){

                                    auth.signOut()
                                    val intent = Intent(this@MainActivity,SignInActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }

                            })
                        setNegativeButton(R.string.cancel
                        ) { _, _ ->
                            // User cancelled the dialog
                            Log.d(TAG, "cancel this logout")
                            Toast.makeText(this@MainActivity,"cancel logout task",Toast.LENGTH_LONG).show()
                        }
                    }
                    // Set other dialog properties

                    // Create the AlertDialog
                    builder.create()
                }

                alertDialog.setCanceledOnTouchOutside(false)
                alertDialog.show()

                return true
            } else ->{
            return super.onOptionsItemSelected(item)
        }
        }
    }
}