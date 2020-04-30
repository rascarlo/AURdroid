package com.rascarlo.aurdroid.infoResult

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rascarlo.aurdroid.databinding.DependencyItemBinding

class DependencyAdapter :
    ListAdapter<String, DependencyAdapter.DependencyAdapterViewHolder>(DiffCallback) {

    private companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return TextUtils.equals(oldItem.trim(), newItem.trim())
        }
    }

    class DependencyAdapterViewHolder(private var binding: DependencyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(string: String) {
            binding.string = string
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DependencyAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DependencyAdapterViewHolder(
            DependencyItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: DependencyAdapterViewHolder, position: Int) {
        val string = getItem(position)
        holder.bind(string)
    }
}