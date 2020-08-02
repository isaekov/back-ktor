package ru.hwru.server.dto.media

import ru.hwru.server.entity.media.MediaModel
import ru.hwru.server.entity.media.MediaType
import java.awt.PageAttributes

data class MediaResponseDto(val id: String, val mediaType: MediaType) {

    companion object {
        fun fromModel(model: MediaModel) = MediaResponseDto(
            id = model.id,
            mediaType = model.mediaType
        )
    }
}