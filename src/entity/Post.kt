package entity

data class Post(
    val id: Long,
    val createDate: String? = null,
    val authorName: String? = null,
    val content: String? = null,
    val likeCount: Long = 0,
    val likeMe: Boolean = false,
    val commentCount: Long = 0,
    val commentMe: Boolean = false,
    val shareCount: Long = 0,
    val shareMe: Boolean = false,
    val image: String? = null,
    val postType: PostType = PostType.EVENT_POST,
    val address: String? = null,
    val coordinates: CoordinateLocation? = null,
    val post: Post? = null,
    val forwardMe:Boolean = false,
    val adSourceImage: String? = null,
    val adHead:String? = null,
    val adImageContent: String? = null,
    val videoUrl: String? = null

)