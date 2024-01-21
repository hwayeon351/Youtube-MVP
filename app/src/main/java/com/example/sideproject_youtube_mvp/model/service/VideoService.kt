package com.example.sideproject_youtube_mvp.model.service

import com.example.sideproject_youtube_mvp.model.dto.VideoDto
import retrofit2.Response
import retrofit2.http.GET

interface VideoService {
    @GET("/v3/1d022ac7-2cfa-4306-80f2-8f31419d8d20")
    suspend fun listVideos(): Response<VideoDto>
}