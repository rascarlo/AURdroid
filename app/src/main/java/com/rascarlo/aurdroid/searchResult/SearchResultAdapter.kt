package com.rascarlo.aurdroid.searchResult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rascarlo.aurdroid.databinding.SearchResultItemBinding
import com.rascarlo.aurdroid.network.SearchResult

/**
 * list adapter for [SearchResultFragment]
 */
class SearchResultAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<SearchResult, SearchResultAdapter.SearchResultViewHolder>(DiffCallback) {

    /**
     * diff util callback for [SearchResult]
     * wrapper into a companion object
     * use [SearchResult] to compare if [areItemsTheSame]
     * use [SearchResult.id] to compare if [areContentsTheSame]
     */
    private companion object DiffCallback : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem.id == newItem.id
        }
    }

    // view holder
    class SearchResultViewHolder(private var binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(searchResult: SearchResult) {
            binding.searchResult = searchResult
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SearchResultViewHolder(
            SearchResultItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val searchResult = getItem(position)
        holder.bind(searchResult)
        holder.itemView.setOnClickListener { onClickListener.onClick(searchResult) }
    }

    class OnClickListener(val clickListener: (searchResult: SearchResult) -> Unit) {
        fun onClick(searchResult: SearchResult) = clickListener(searchResult)
    }
}