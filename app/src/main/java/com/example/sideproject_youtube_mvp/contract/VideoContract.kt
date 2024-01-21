package com.example.sideproject_youtube_mvp.contract

import android.content.Context
import com.example.sideproject_youtube_mvp.model.dto.VideoDto
import com.google.android.exoplayer2.source.ProgressiveMediaSource

interface VideoContract {
    interface View : BaseContract.View {
        fun updateVideoList(videoDto: VideoDto)
        fun playVideo(title: String, url: String)
    }

    interface Presenter<T : BaseContract.View> : BaseContract.Presenter<T>  {
        fun init()
        fun getVideoList()
        fun deInit()
        fun getMediaSource(context: Context, url: String) : ProgressiveMediaSource
        fun setPlayerModel(title: String, videoUrl: String)
        fun changePlayerState()
    }
}