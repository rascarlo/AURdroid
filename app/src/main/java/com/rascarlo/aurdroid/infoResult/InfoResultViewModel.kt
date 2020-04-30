package com.rascarlo.aurdroid.infoResult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rascarlo.aurdroid.network.AurDroidApiStatus
import com.rascarlo.aurdroid.network.AurWebApi
import com.rascarlo.aurdroid.network.InfoResult
import com.rascarlo.aurdroid.utils.Constants
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

class InfoResultViewModel(name: String) : ViewModel() {

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
    private val _infoResult = MutableLiveData<InfoResult>()

    // exposed immutable LiveData for the result of the most recent request
    val infoResult: LiveData<InfoResult>
        get() = _infoResult

    init {
        getInfo(name)
    }

    private fun getInfo(name: String) {
        viewModelScope.launch {
            val response = AurWebApi.retrofitService.getInfo(name = name)
            try {
                _status.value = AurDroidApiStatus.LOADING
                when (response.type) {
                    // response successful, type multiinfo
                    Constants.RETURN_TYPE_MULTIINFO -> {
                        if (null != response.resultCount && response.resultCount >= 1
                            && null != response.results && response.results.isNotEmpty()
                        ) {
                            // response list is not empty
                            _infoResult.value = response.results[0]
                            _status.value = AurDroidApiStatus.DONE
                        } else {
                            // response list is empty
                            _infoResult.value = null
                            _status.value = AurDroidApiStatus.NO_PACKAGE_FOUND
                        }
                    }
                    // response successful, type error
                    Constants.RETURN_TYPE_ERROR -> {
                        _infoResult.value = null
                        _status.value = AurDroidApiStatus.ERROR
                        _error.value = response.error

                    }
                    // response unsuccessful
                    else -> {
                        _infoResult.value = null
                        _status.value = AurDroidApiStatus.ERROR
                    }
                }
                // exception
            } catch (e: Exception) {
                Timber.e(e)
                _infoResult.value = null
                _status.value = AurDroidApiStatus.ERROR
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}