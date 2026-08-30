package com.zernex.video.files

import com.zernex.video.data.VideoItem

sealed class FileAction {
    data class Delete(val video: VideoItem) : FileAction()
    data class Rename(val video: VideoItem, val newName: String) : FileAction()
    data class Share(val video: VideoItem) : FileAction()
    data class Inspect(val video: VideoItem) : FileAction()
}
