package com.example.sideproject_youtube_mvp.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sideproject_youtube_mvp.R
import com.example.sideproject_youtube_mvp.contract.VideoContract
import com.example.sideproject_youtube_mvp.databinding.FragmentPlayerBinding
import com.example.sideproject_youtube_mvp.model.dto.VideoDto
import com.example.sideproject_youtube_mvp.presenter.VideoPresenter
import com.example.sideproject_youtube_mvp.utils.Utils
import com.example.sideproject_youtube_mvp.view.adapter.VideoAdapter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlin.math.abs

class PlayerFragment : Fragment(R.layout.fragment_player), VideoContract.View {

    private val presenter: VideoContract.Presenter<VideoContract.View> by lazy {
        VideoPresenter(this)
    }

    private var binding: FragmentPlayerBinding? = null

    private lateinit var videoAdapter: VideoAdapter
    private var player: SimpleExoPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayerBinding.bind(view)

        presenter.init()

        initMotionLayoutEvent()
        initVideoListRecyclerView()
        initPlayer()
        initControlButton()
    }

    private fun initMotionLayoutEvent() {
        Log.d(TAG, "initMotionLayoutEvent()")
        binding!!.playerMotionLayout.setTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                Log.d(TAG, "onTransitionChange()")
                binding.let {
                    (activity as MainActivity).also { mainActivity ->
                        mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress =
                            abs(progress)
                    }
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {}

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        })
    }

    private fun initVideoListRecyclerView() {
        Log.d(TAG, "initRecyclerView()")
        videoAdapter = VideoAdapter(callback = { url, title ->
            playVideo(title, url)
        })

        binding!!.fragmentRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }

        presenter.getVideoList()
    }

    override fun updateVideoList(videoDto: VideoDto) {
        Log.d(TAG, "updateVideoList()")
        videoAdapter.submitList(videoDto.videos)
    }

    private fun initPlayer() {
        Log.d(TAG, "initPlayer()")
        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }

        binding!!.playerView.let {
            it.player = player
            it.useController = false
        }

        binding?.let {
            player?.addListener(object : Player.EventListener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    Log.d(TAG, "onIsPlayingChanged()")
                    super.onIsPlayingChanged(isPlaying)

                    /** Presenter로 위임 가능!!*/
                    if (isPlaying) {
                        it.bottomPlayerControlButton.setImageResource(Utils.PAUSE_ICON)
                    } else {
                        it.bottomPlayerControlButton.setImageResource(Utils.PLAY_ICON)
                    }
                }
            })
        }
    }

    private fun initControlButton() {
        Log.d(TAG, "initControlButton()")
        binding!!.bottomPlayerControlButton.setOnClickListener {
            Log.d(TAG, "bottomPlayerControlButton OnClicked!!")
            val player = this.player ?: return@setOnClickListener

            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    override fun playVideo(title: String, url: String) {
        Log.d(TAG, "playVideo()")
        context?.let { context ->
            player?.let {
                it.setMediaSource(getMediaSource(url)!!)
                it.prepare()
                it.play()
            }
        }
        binding!!.let {
            it.playerMotionLayout.transitionToEnd()
            it.bottomTitleTextView.text = title
        }
    }

    fun getMediaSource(url: String): ProgressiveMediaSource? {
        Log.d(TAG, "getMediaSource()")
        context?.let {
            val dataSourceFactory = DefaultDataSourceFactory(it)
            return ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
        }
        return null
    }

    override fun onStop() {
        Log.d(TAG, "onStop()")
        super.onStop()

        player?.pause()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()

        binding = null
        player?.release()
        presenter.deInit()
        presenter.onDestroy()
    }

    companion object {
        private const val TAG = "PlayerFragment"
    }
}