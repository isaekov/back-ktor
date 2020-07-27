package entity

data class Post(
    var id: Long,
    var createDate: String,
    var authorName: String,
    var content: String,
    var likeCount: Long,
    var likeMe: Boolean,
    var commentCount: Long,
    var commentMe: Boolean,
    var shareCount: Long,
    var shareMe: Boolean,
    var image: String,
    var postType: PostType = PostType.EVENT_POST,
    var address: String? = null,
    var coordinates: CoordinateLocation? = null,
    var post: Post? = null,
    var repostMe:Boolean = false,
    var adSourceImage: String? = null,
    var adHead:String? = null,
    var adImageContent: String? = null,
    var videoUrl: String? = null

)