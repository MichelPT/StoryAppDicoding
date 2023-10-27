package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.ItemRowBinding

private lateinit var onItemClickCallback: StoryListAdapter.OnItemClickCallBack

class StoryListAdapter :
    PagingDataAdapter<ListStoryItem, StoryListAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: ItemRowBinding =
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val story = getItem(position)
        if (story!=null){
            holder.bind(story)
            if (::onItemClickCallback.isInitialized){
                holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(story) }
            }
        }
    }
    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        onItemClickCallback = onItemClickCallBack
    }

    class ItemViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.apply {
                nameTextView.text = story.name
                descTextView.text = story.description
                Glide.with(root)
                    .load(story.photoUrl)
                    .into(profileImageView)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(story: ListStoryItem)
    }
}