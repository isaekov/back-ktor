package ru.hwru.server.entity.media

import entity.Attachment


enum class MediaType {
    IMAGE
}

data class MediaModel(val id: String, val mediaType: MediaType) : Attachment