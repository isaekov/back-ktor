package entity

data class Post(
    var id: Long,
    var createDate: String? = null,
    var authorName: String? = null,
    var content: String? = null,
    var likeCount: Long = 0,
    var likeMe: Boolean = false,
    var commentCount: Long = 0,
    var commentMe: Boolean = false,
    var shareCount: Long = 0,
    var shareMe: Boolean = false,
    var image: String? = null,
    var postType: PostType = PostType.EVENT_POST,
    var address: String? = null,
    var coordinates: CoordinateLocation? = null,
    var post: Post? = null,
    var forwardMe:Boolean = false,
    var adSourceImage: String? = null,
    var adHead:String? = null,
    var adImageContent: String? = null,
    var videoUrl: String? = null

)