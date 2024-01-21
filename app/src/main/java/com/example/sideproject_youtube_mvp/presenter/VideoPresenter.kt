package com.example.sideproject_youtube_mvp.presenter

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.sideproject_youtube_mvp.contract.VideoContract
import com.example.sideproject_youtube_mvp.model.PlayerModel
import com.example.sideproject_youtube_mvp.model.service.RetrofitClient
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.*

class VideoPresenter(override var view: VideoContract.View?) :
    VideoContract.Presenter<VideoContract.View> {
    private lateinit var job: CoroutineScope

    override fun init() {
        Log.d(TAG, "onDestroy()")
        job = CoroutineScope(Dispatchers.Main)

        //IO로 돌리면 에러.... WHY
        //job = CoroutineScope(Dispatchers.IO)
    }

    override fun deInit() {
        Log.d(TAG, "deInit()")
        job.cancel()
    }

    override fun getVideoList() {
        Log.d(TAG, "getVideoList()")
        job.launch {
            val response = RetrofitClient.videoService.listVideos()
            if (response.isSuccessful) {
                response.body()?.let { videoDto ->
                    Log.d(TAG, "Response Success!!")
                    view?.updateVideoList(videoDto)
                }
            } else {
                Log.d(TAG, "Response Fail!!")
            }
        }
    }

    companion object {
        private const val TAG = "VideoPresenter"
    }
}