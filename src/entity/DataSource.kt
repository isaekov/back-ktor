package entity

class DataSource {
    companion object {
        fun createDataSet(): ArrayList<Post> {
            val list = ArrayList<Post>()



            list.add(
                Post(
                    id = 1,
                    createDate = "1231231232",
                    authorName = "Белла Ахмадулина",
                    content = "Юная поэтесса с гражданской...",
                    likeMe = true,
                    likeCount = 13,
                    commentCount = 2,
                    commentMe = false,
                    shareCount = 1,
                    shareMe = false,
                    address = "Kazan",
                    image = "https://b1.culture.ru/c/689859.150x225.jpg",
                    coordinates = CoordinateLocation(latitude = "123124", longitude = "3421343"),
                    postType = PostType.POST
                )
            )

            list.add(
                Post(
                    id = 2,
                    authorName = "Группа энтузиастов",
                    createDate = "1231231232",
                    content = "Литература",
                    likeMe = false,
                    likeCount = 1,
                    commentCount = 4,
                    commentMe = false,
                    shareCount = 67,
                    shareMe = false,
                    image = "https://picsum.photos/200/300",
                    address = "Kazan",
                    coordinates = CoordinateLocation(latitude = "123124", longitude = "3421343"),
                    post = list[0],
                    postType = PostType.REPOST_POST
                )
            )

            list.add(
                Post(
                    id = 3,
                    createDate = "1231231232",
                    authorName = "Просто пост",
                    content = "Просто пост и контент",
                    likeMe = true,
                    likeCount = 45,
                    commentCount = 89,
                    commentMe = false,
                    shareCount = 5,
                    shareMe = false,
                    image = "https://picsum.photos/200",
                    address = "Kazan",
                    postType = PostType.POST

                )
            )

            list.add(
                Post(
                    id = 4,
                    createDate = "1231231232",
                    authorName = "",
                    content = "Путешествие",
                    likeMe = false,
                    likeCount = 1,
                    commentCount = 2,
                    commentMe = false,
                    shareCount = 1,
                    shareMe = false,
                    image = "https://the-geek.ru/wp-content/uploads/2015/09/daily-2.jpg",
                    address = "Kazan",
                    adHead = "Google",
                    adImageContent = "https://www.gstatic.com/webp/gallery/5.webp",
                    repostMe = true,
                    postType = PostType.AD_POST,
                    videoUrl = "https://www.krugosvet.ru/enc/strany_mira/IRLANDSKAYA_RESPUBLIKA.html"

                )
            )

            list.add(
                Post(
                    id = 5,
                    createDate = "1590231232",
                    authorName = "Фильмы для отдыха",
                    content = "Весна",
                    likeMe = false,
                    likeCount = 5,
                    commentCount = 48,
                    commentMe = false,
                    shareCount = 1,
                    shareMe = false,
                    image = "https://picsum.photos/200",
                    address = "Kazan",
                    adImageContent = "https://img.youtube.com/vi/WhWc3b3KhnY/hqdefault.jpg",
                    repostMe = true,
                    postType = PostType.VIDEO_POST,
                    videoUrl = "https://www.youtube.com/watch?v=WhWc3b3KhnY"

                )
            )

            list.add(
                Post(
                    id = 6,
                    createDate = "1231231232",
                    authorName = "Путешествие",
                    content = "Путешествие – это не только фотографии на память. Путешествия расширяют кругозор и меняют нас в корне. 11 простых истин о том, как это ...",
                    likeMe = false,
                    likeCount = 9,
                    commentCount = 0,
                    commentMe = true,
                    shareCount = 1,
                    shareMe = false,
                    image = "https://picsum.photos/200",
                    address = "Москва",
                    repostMe = true,
                    postType = PostType.EVENT_POST,
                    coordinates = CoordinateLocation(latitude = "123124", longitude = "3421343")

                )
            )

            return list
        }
    }
}