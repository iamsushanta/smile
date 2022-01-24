package iamzen.`in`.smile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import iamzen.`in`.smile.model.Post

class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var userName: TextView = itemView.findViewById(R.id.userName)
    var userImage: ImageView = itemView.findViewById(R.id.userImage)
    var createdAt:TextView = itemView.findViewById(R.id.createdAt)
    var postTitle:TextView = itemView.findViewById(R.id.postTitle)
    var likeButton:ImageView = itemView.findViewById(R.id.likeButton)
    var likeCount:TextView = itemView.findViewById(R.id.likeCount)


}
class PostAdapter(options: FirestoreRecyclerOptions<Post>,var listener:IPostLike) :FirestoreRecyclerAdapter<Post,PostViewHolder>(
    options
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val postViewHolder = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_story,parent,false))
        postViewHolder.likeButton.setOnClickListener{
            listener.postLikeTab(snapshots.getSnapshot(postViewHolder.adapterPosition).id)
        }
        return postViewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.postTitle.text = model.text
        holder.userName.text = model.createdBy.userName
        Glide.with(holder.userImage.context).load(model.createdBy.userImageUrl).circleCrop().into(holder.userImage)
        holder.likeCount.text = model.like.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)


        val auth = Firebase.auth
        val currentUserUid = auth.currentUser!!.uid
        val isLike = model.like.contains(currentUserUid)

        if (isLike){
            holder.likeButton.setImageResource(R.drawable.ic_baseline_favoritered_24)
        } else{
            holder.likeButton.setImageResource(R.drawable.ic_baseline_favoritegrey_24)
        }
    }
}

interface IPostLike{
    fun postLikeTab(postId:String)
}