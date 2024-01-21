package com.example.sideproject_youtube_mvp.model

import com.example.sideproject_youtube_mvp.R

data class PlayerModel(
    var title: String,
    var videoUrl: String,
    var isPlaying: Boolean = false,
    var buttonImg: Int = R.drawable.ic_baseline_play_arrow_24
)