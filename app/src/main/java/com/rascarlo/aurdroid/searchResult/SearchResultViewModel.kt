package com.rascarlo.aurdroid.searchResult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rascarlo.aurdroid.network.AurDroidApiStatus
import com.rascarlo.aurdroid.network.AurWebApi
import com.rascarlo.aurdroid.network.SearchResult
import com.rascarlo.aurdroid.utils.Constants
import com.rascarlo.aurdroid.utils.sortSearchResultList
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchResultViewModel(field: String, keyword: String, sort: Int) : ViewModel() {

    // internal mutable LiveData for the error of the most recent request
    private val _error = MutableLiveData<String>()

    // exposed immutable LiveData for the error of the most recent request
    val error: LiveData<String>
        get() = _error

    // internal mutable LiveData for the status of the most recent request
    private val _status = MutableLiveData<AurDroidApiStatus>()

    // exposed immutable LiveData for the status of the most recent request
    val status: LiveData<AurDroidApiStatus>
        get() = _status

    // internal mutable LiveData for the result of the most recent request
    private val _searchResultList = MutableLiveData<List<SearchResult>>()

    // exposed immutable LiveData for the result of the most recent request
    val searchResultList: LiveData<List<SearchResult>>
        get() = _searchResultList

    // internal mutable LiveData to navigate to fragment
    private val _displayPackage = MutableLiveData<SearchResult>()

    // exposed immutable LiveData to navigate to fragment
    val displayPackage: LiveData<SearchResult>
        get() = _displayPackage

    init {
        getPackages(field, keyword, sort)
    }

    private fun getPackages(field: String, keyword: String, sort: Int) {
        viewModelScope.launch {
            val response = AurWebApi.retrofitService.getPackages(
                field = field,
                keyword = keyword
            )
            try {
                _status.value = AurDroidApiStatus.LOADING
                when (response.type) {
                    // response successful, type search
                    Constants.RETURN_TYPE_SEARCH -> {
                        if (response.resultCount != null && response.resultCount > 0
                            && response.results != null && response.results.isNotEmpty()
                        ) {
                            // response list is not empty
                            _searchResultList.value = sortSearchResultList(response.results, sort)
                            _status.value = AurDroidApiStatus.DONE
                        } else {
                            // response list is empty
                            _searchResultList.value = null
                            _status.value = AurDroidApiStatus.NO_PACKAGE_FOUND
                        }
                    }
                    // response successful, type error
                    Constants.RETURN_TYPE_ERROR -> {
                        _searchResultList.value = null
                        _status.value = AurDroidApiStatus.RETURN_TYPE_ERROR
                        _error.value = response.error
                    }
                    // response unsuccessful
                    else -> {
                        _searchResultList.value = null
                        _status.value = AurDroidApiStatus.ERROR
                    }
                }
                // exception
            } catch (e: Exception) {
                Timber.e(e)
                _searchResultList.value = null
                _status.value = AurDroidApiStatus.ERROR
            }
        }
    }

    fun sortList(sort: Int) {
        _searchResultList.value = (_searchResultList.value?.let { sortSearchResultList(it, sort) })
    }

    fun displaySelectedPackage(searchResult: SearchResult) {
        _displayPackage.value = searchResult
    }

    fun displaySelectedPackageComplete() {
        _displayPackage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}