package com.rascarlo.aurdroid.searchResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchResultViewModelFactory(
    private val keyword: String,
    private val field: String,
    private val sort: Int
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(SearchResultViewModel::class.java)) {
            return SearchResultViewModel(
                keyword = this.keyword,
                field = this.field,
                sort = this.sort
            ) as T
        }
        throw IllegalArgumentException("Dude, that is NOT the view model class I was expecting")
    }
}