package iamzen.`in`.smile.model

data class Post(val text:String="",
                val createdBy:User = User(),
                val createdAt:Long = 0,
                val like:ArrayList<String> = ArrayList()
)
