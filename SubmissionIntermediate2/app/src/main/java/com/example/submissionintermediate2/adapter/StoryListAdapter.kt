package com.example.submissionintermediate2.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionintermediate2.databinding.StoryListAdapterLayoutBinding
import com.example.submissionintermediate2.model.StoryModel
import com.example.submissionintermediate2.view.storydetail.StoryDetailActivity
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

class StoryListAdapter : PagingDataAdapter<StoryModel, StoryListAdapter.StoryListViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    class StoryListViewHolder(val binding: StoryListAdapterLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryListViewHolder {
        return StoryListViewHolder(StoryListAdapterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StoryListViewHolder, position: Int) {
        with(holder) {
            val story = getItem(position)
            if (story != null) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .circleCrop()
                    .into(binding.ivItemPhoto)
                binding.tvItemName.text = story.name
                binding.tvItemDescription.text = story.description

                holder.itemView.setOnClickListener {
                    val activity = itemView.context
                    val intent = Intent(itemView.context, StoryDetailActivity::class.java)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity as Activity,
                            Pair(binding.ivItemPhoto, "photo"),
                            Pair(binding.tvItemName, "name"),
                            Pair(binding.tvItemDescription, "description")
                        )
                    intent.putExtra(StoryDetailActivity.STORY_DETAIL, story)
                    activity.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }
}