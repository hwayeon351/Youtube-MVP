package com.example.sideproject_youtube_mvp.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sideproject_youtube_mvp.databinding.ItemVideoBinding
import com.example.sideproject_youtube_mvp.model.VideoModel
import com.example.sideproject_youtube_mvp.presenter.VideoPresenter

class VideoAdapter(val callback: (String, String) -> Unit) :
    ListAdapter<VideoModel, VideoAdapter.ViewHolder>(diffUtil) {
    private lateinit var binding: ItemVideoBinding

    inner class ViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoModel) {
            Log.d(TAG, "ViewHolder.bind()")
            binding.titleTextView.text = item.title
            binding.subtitleTextView.text = item.subtitle

            Glide.with(binding.thumbnailImageView.context)
                .load(item.thumb)
                .into(binding.thumbnailImageView)

            binding.root.setOnClickListener {
                callback(item.sources, item.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder()")
        binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder()")
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<VideoModel>() {
            override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
                Log.d(TAG, "DiffUtil.ItemCallback.areItemsTheSame()")
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
                Log.d(TAG, "DiffUtil.ItemCallback.areContentsTheSame()")
                return oldItem.sources == newItem.sources
            }

        }

        private const val TAG = "VideoAdapter"
    }
}