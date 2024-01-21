package com.example.sideproject_youtube_mvp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sideproject_youtube_mvp.R
import com.example.sideproject_youtube_mvp.contract.VideoContract
import com.example.sideproject_youtube_mvp.databinding.ActivityMainBinding
import com.example.sideproject_youtube_mvp.model.dto.VideoDto
import com.example.sideproject_youtube_mvp.presenter.VideoPresenter
import com.example.sideproject_youtube_mvp.view.adapter.VideoAdapter

class MainActivity : AppCompatActivity(), VideoContract.View {

    private val presenter: VideoContract.Presenter<VideoContract.View> by lazy {
        VideoPresenter(this)
    }
    private var binding: ActivityMainBinding? = null

    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        presenter.init()

        initPlayerFragment()
        initVideoListRecyclerView()
    }

    private fun initPlayerFragment() {
        Log.d(TAG, "initPlayerFragment()!!")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PlayerFragment())
            .commit()
    }

    private fun initVideoListRecyclerView() {
        Log.d(TAG, "initVideoListRecyclerView()")
        videoAdapter = VideoAdapter(callback = { url, title ->
            playVideo(title, url)
        })

        binding!!.mainRecyclerView.apply {
            this.adapter = videoAdapter
            this.layoutManager = LinearLayoutManager(context)
        }

        presenter.getVideoList()
    }

    override fun updateVideoList(videoDto: VideoDto) {
        Log.d(TAG, "updateVideoList()")
        videoAdapter.submitList(videoDto.videos)
    }

    override fun playVideo(title: String, url: String) {
        Log.d(TAG, "playVideo()")
        supportFragmentManager.fragments.find { it is PlayerFragment }?.let {
            (it as PlayerFragment).playVideo(title, url)
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()

        binding = null
        presenter.deInit()
        presenter.onDestroy()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}